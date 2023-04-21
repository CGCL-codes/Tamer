package org.openconcerto.erp.utils.correct;

import org.openconcerto.sql.changer.Changer;
import org.openconcerto.sql.model.DBRoot;
import org.openconcerto.sql.model.DBSystemRoot;
import org.openconcerto.sql.model.SQLBase;
import org.openconcerto.sql.model.SQLField;
import org.openconcerto.sql.model.SQLName;
import org.openconcerto.sql.model.SQLSelect;
import org.openconcerto.sql.model.SQLSelect.ArchiveMode;
import org.openconcerto.sql.model.SQLSystem;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.sql.model.Where;
import org.openconcerto.sql.request.UpdateBuilder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Unarchive ECRITURE to balance MOUVEMENT, then link SAISIE_KM_ELEMENT and a unique matching
 * ECRITURE.
 * 
 * @author Sylvain CUAZ
 */
public class CorrectMouvement extends Changer<DBRoot> {

    public CorrectMouvement(DBSystemRoot b) {
        super(b);
    }

    @Override
    protected void changeImpl(DBRoot societeRoot) throws SQLException {
        final SQLTable ecritureT = societeRoot.getTable("ECRITURE");
        {
            final SQLField ecritureMvtFF = ecritureT.getField("ID_MOUVEMENT");
            final SQLSelect selUnbalanced = new SQLSelect(societeRoot.getBase());
            selUnbalanced.addSelect(ecritureMvtFF);
            selUnbalanced.addGroupBy(ecritureMvtFF);
            selUnbalanced.setHaving(Where.quote(societeRoot.getBase().quote("SUM(%n) != SUM(%n)", ecritureT.getField("DEBIT"), ecritureT.getField("CREDIT"))));
            final SQLSelect selUnfixable = new SQLSelect(selUnbalanced);
            selUnfixable.setArchivedPolicy(ArchiveMode.BOTH);
            final String selFixableUnbalanced = "( " + selUnbalanced.asString() + "\nEXCEPT\n" + selUnfixable.asString() + " )";
            final UpdateBuilder updateUnbalanced = new UpdateBuilder(ecritureT);
            updateUnbalanced.addTable(selFixableUnbalanced, SQLBase.quoteIdentifier("semiArchivedMvt"));
            updateUnbalanced.setWhere(Where.quote("%i = %f", new SQLName("semiArchivedMvt", "ID_MOUVEMENT"), ecritureMvtFF));
            updateUnbalanced.set(ecritureT.getArchiveField().getName(), "0");
            getDS().execute(updateUnbalanced.asString());
        }
        {
            final SQLTable saisieKmElemT = societeRoot.getGraph().findReferentTable(ecritureT, "SAISIE_KM_ELEMENT");
            final SQLTable saisieKmT = saisieKmElemT.getForeignTable("ID_SAISIE_KM");
            final SQLSelect selIdentifiableNonUsed = new SQLSelect(societeRoot.getBase());
            final List<String> uniqueFields = Arrays.asList("ID_MOUVEMENT", "DEBIT", "CREDIT");
            selIdentifiableNonUsed.addAllSelect(ecritureT, uniqueFields);
            final String quotedID = ecritureT.getKey().getSQLName(ecritureT).quote();
            final String uniqueID;
            if (getSyntax().getSystem() == SQLSystem.POSTGRESQL) uniqueID = "(array_agg(" + quotedID + "))[1]"; else uniqueID = "cast(GROUP_CONCAT(" + quotedID + ") as integer)";
            final String uniqueIDAlias = "ID";
            selIdentifiableNonUsed.addRawSelect(uniqueID, uniqueIDAlias);
            selIdentifiableNonUsed.addBackwardJoin("LEFT", null, saisieKmElemT.getField("ID_ECRITURE"), null);
            selIdentifiableNonUsed.setWhere(Where.isNull(saisieKmElemT.getKey()));
            for (final String uniqField : uniqueFields) selIdentifiableNonUsed.addGroupBy(ecritureT.getField(uniqField));
            selIdentifiableNonUsed.setHaving(Where.createRaw("count(*) = 1"));
            final UpdateBuilder update = new UpdateBuilder(saisieKmElemT);
            update.addTable(saisieKmT.getSQLName().quote(), null);
            update.addTable("( " + selIdentifiableNonUsed.asString() + " )", "e");
            final Where joinSaisieKmW = new Where(saisieKmElemT.getField("ID_SAISIE_KM"), "=", saisieKmT.getKey());
            Where joinEcritureW = null;
            for (final String uniqField : uniqueFields) {
                final SQLTable t = uniqField.equals("ID_MOUVEMENT") ? saisieKmT : saisieKmElemT;
                joinEcritureW = Where.quote("e." + SQLBase.quoteIdentifier(uniqField) + "= %f", t.getField(uniqField)).and(joinEcritureW);
            }
            final Where dontOverwrite = new Where(saisieKmElemT.getField("ID_ECRITURE"), Where.NULL_IS_DATA_EQ, ecritureT.getUndefinedIDNumber());
            final Where dontUpdateUndef = new Where(saisieKmElemT.getKey(), Where.NULL_IS_DATA_NEQ, saisieKmElemT.getUndefinedIDNumber());
            final Where unarchived = new Where(saisieKmElemT.getArchiveField(), "=", 0);
            update.setWhere(joinSaisieKmW.and(joinEcritureW).and(dontOverwrite).and(dontUpdateUndef).and(unarchived));
            update.set("ID_ECRITURE", "e." + SQLBase.quoteIdentifier(uniqueIDAlias));
            getDS().execute(update.asString());
        }
    }
}
