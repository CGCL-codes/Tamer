package org.fudaa.dodico.crue.io.rptg;

import java.util.List;
import org.fudaa.ctulu.CtuluLog;
import org.fudaa.dodico.crue.io.common.CrueConverterContexteSimulation;
import org.fudaa.dodico.crue.io.common.CrueConverterRef;
import org.fudaa.dodico.crue.io.common.CrueConverterVariableRes;
import org.fudaa.dodico.crue.io.common.CrueData;
import org.fudaa.dodico.crue.io.common.Ref;
import org.fudaa.dodico.crue.io.common.VariableRes.VariableResLoi;
import org.fudaa.dodico.crue.io.common.VariableResRef;
import org.fudaa.dodico.crue.io.common.VariableResRef.VariableResLoiRef;
import org.fudaa.dodico.crue.io.dao.CrueConverter;
import org.fudaa.dodico.crue.io.rptg.RPTGFile.Branches;
import org.fudaa.dodico.crue.io.rptg.RPTGFile.Casiers;
import org.fudaa.dodico.crue.io.rptg.RPTGFile.ResultatsPrtGeo;
import org.fudaa.dodico.crue.io.rptg.RPTGFile.Sections;
import org.fudaa.dodico.crue.io.rptg.RPTGFile.SectionsVariables;

public class CrueConverterRPTG implements CrueConverter<CrueDaoRPTG, RPTGFile> {

    private static final CrueConverterContexteSimulation contexteSimulationConverter = new CrueConverterContexteSimulation();

    private static final CrueConverterRef refConverter = new CrueConverterRef();

    private static final CrueConverterVariableRes variableResConverter = new CrueConverterVariableRes();

    @Override
    public RPTGFile convertDaoToMetier(CrueDaoRPTG dao, CrueData dataLinked, CtuluLog analyser) {
        RPTGFile metier = new RPTGFile();
        metier.setCommentaire(dao.getCommentaire());
        metier.setContextSimulation(contexteSimulationConverter.convertDaoToMetier(dao.ContexteSimulation));
        refConverter.convertDaoToMetier(dao.StructureResultats.Noeuds.NoeudsNiveauContinu, metier.getNoeuds());
        final Casiers casiers = new Casiers();
        variableResConverter.convertDaoToMetier(dao.StructureResultats.Casiers.Variables, casiers.getVariablesRes(), casiers.getVariablesResLoi());
        refConverter.convertDaoToMetier(dao.StructureResultats.Casiers.CasiersProfil, casiers.getCasiersProfil());
        metier.setCasiers(casiers);
        Sections sections = new Sections();
        sections.setSectionsIdem(this.convertDaoToMetierSectionsVariables(dao.StructureResultats.Sections.SectionsIdem));
        sections.setSectionsInterpolee(this.convertDaoToMetierSectionsVariables(dao.StructureResultats.Sections.SectionsInterpolee));
        sections.setSectionsProfil(this.convertDaoToMetierSectionsVariables(dao.StructureResultats.Sections.SectionsProfil));
        refConverter.convertDaoToMetier(dao.StructureResultats.Sections.SectionsSansGeometrie, sections.getSectionsSansGeometrie());
        metier.setSections(sections);
        Branches branches = new Branches();
        refConverter.convertDaoToMetier(dao.StructureResultats.Branches.BranchesBarrageFilEau, branches.getBranchesBarrageFilEau());
        refConverter.convertDaoToMetier(dao.StructureResultats.Branches.BranchesBarrageGenerique, branches.getBranchesBarrageGenerique());
        refConverter.convertDaoToMetier(dao.StructureResultats.Branches.BranchesNiveauxAssocies, branches.getBranchesNiveauxAssocies());
        refConverter.convertDaoToMetier(dao.StructureResultats.Branches.BranchesOrifice, branches.getBranchesOrifice());
        refConverter.convertDaoToMetier(dao.StructureResultats.Branches.BranchesPdc, branches.getBranchesPdc());
        refConverter.convertDaoToMetier(dao.StructureResultats.Branches.BranchesSaintVenant, branches.getBranchesSaintVenant());
        refConverter.convertDaoToMetier(dao.StructureResultats.Branches.BranchesSeuilLateral, branches.getBranchesSeuilLateral());
        refConverter.convertDaoToMetier(dao.StructureResultats.Branches.BranchesSeuilTransversal, branches.getBranchesSeuilTransversal());
        refConverter.convertDaoToMetier(dao.StructureResultats.Branches.BranchesStrickler, branches.getBranchesStrickler());
        metier.setBranches(branches);
        ResultatsPrtGeo resultats = new ResultatsPrtGeo();
        resultats.setHref(dao.ResultatsPrtGeos.ResultatsPrtGeo.href);
        resultats.setOffset(dao.ResultatsPrtGeos.ResultatsPrtGeo.offset);
        metier.setResultatsPrtGeo(resultats);
        return metier;
    }

    private SectionsVariables convertDaoToMetierSectionsVariables(List<Ref> dao) {
        SectionsVariables metier = new SectionsVariables();
        for (Ref ref : dao) {
            if (ref instanceof VariableResLoiRef) {
                metier.getVariablesResLoi().add((VariableResLoi) variableResConverter.convertDaoToMetier((VariableResRef) ref));
            } else if (ref instanceof VariableResRef) {
                metier.getVariablesRes().add(variableResConverter.convertDaoToMetier((VariableResRef) ref));
            } else {
                metier.getSections().add(refConverter.convertDaoToMetier(ref));
            }
        }
        return metier;
    }

    @Override
    public RPTGFile getConverterData(CrueData in) {
        return null;
    }

    @Override
    public CrueDaoRPTG convertMetierToDao(RPTGFile metier, CtuluLog analyser) {
        return null;
    }
}
