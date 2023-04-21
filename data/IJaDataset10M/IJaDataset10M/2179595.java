package com.gorillalogic.dal.model.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.dal.model.*;
import com.gorillalogic.test.*;

public class ModelUtils {

    static void assignRoleNames(Model model) throws AccessException {
        Association.Itr ascnItr = model.associationItr();
        while (ascnItr.next()) {
            Participant.Itr partItr = ascnItr.participants();
            int pc = partItr.targetRowCount();
            if (pc != 2) {
                String ascnName = ascnItr.getName();
                throw new StructureException("Association \"" + ascnName + "\" has " + pc + " participants");
            }
            partItr.next();
            Participant.Row part1Row = partItr.asParticipantRow();
            partItr.next();
            Participant.Row part2Row = partItr.asParticipantRow();
            partItr.release();
            assignRoleName(model, ascnItr, part1Row, part2Row);
            assignRoleName(model, ascnItr, part2Row, part1Row);
        }
    }

    private static void assignRoleName(Model model, Association.Row ascnRow, Participant.Row partRow, Participant.Row inverseRow) throws AccessException {
        String name = partRow.getName();
        if (name == null || name.trim().length() == 0) {
            Entity.Row type = partRow.type();
            name = type.getName();
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1, name.length());
            final String root = name;
            int conflictCount = 0;
            while (familyConflict(model, ascnRow, inverseRow.type(), name, 0)) {
                name = root + ++conflictCount;
            }
            partRow.setName(name);
        }
    }

    private static boolean familyConflict(Model model, Association.Row ascnRow, Entity.Row row, String name, int direction) throws AccessException {
        Field.Itr fieldItr = row.owns();
        while (fieldItr.next()) {
            String fieldName = fieldItr.getName();
            if (fieldName.equals(name)) {
                return true;
            }
        }
        Participant.Itr partItr = model.participantItr();
        while (partItr.next()) {
            if (partItr.type().equals(row)) {
                String in = getPartInverse(partItr).getName();
                if (in != null && in.equals(name)) {
                    return true;
                }
            }
        }
        if (direction >= 0) {
            Entity.Itr superItr = row.superType();
            while (superItr.next()) {
                if (familyConflict(model, ascnRow, superItr, name, 1)) return true;
            }
        }
        if (direction <= 0) {
            Entity.Itr subItr = row.subType();
            while (subItr.next()) {
                if (familyConflict(model, ascnRow, subItr, name, -1)) return true;
            }
        }
        return false;
    }

    private static Participant.Row getPartInverse(Participant.Row part) throws AccessException {
        Association.Row ascnRow = part.in();
        Participant.Itr partItr = ascnRow.participants();
        partItr.next();
        if (partItr.equals(part)) {
            partItr.next();
        }
        return partItr;
    }

    public static void testAssignRoleName(Tester tester) throws AccessException {
        final World dataWorld = Universe.factory.newWorldFromNewDefaultModel();
        final World modelWorld = dataWorld.getUniverse().getModelWorld();
        final PkgTable modelPkg = modelWorld.getRootPkg();
        final Model model = modelWorld.getSpawnedUniverse().getModel();
        Txn.mgr.begin();
        Table.Row fooRow = newType(modelPkg, "Foo");
        Table.Row barRow = newType(modelPkg, "Bar");
        associate(modelPkg, fooRow, barRow, true);
        model.assignRoleNames();
        Txn.mgr.commit();
        testFor(tester, model, "foo");
        testFor(tester, model, "bar");
        Txn.mgr.begin();
        associate(modelPkg, fooRow, barRow, true);
        model.assignRoleNames();
        Txn.mgr.commit();
        testFor(tester, model, "foo1");
        testFor(tester, model, "bar1");
        Txn.mgr.begin();
        associate(modelPkg, fooRow, barRow, true);
        model.assignRoleNames();
        Txn.mgr.commit();
        testFor(tester, model, "foo2");
        testFor(tester, model, "bar2");
        Table table = dataWorld.getRootPkg();
        Txn.mgr.begin();
        Table.Row subFooRow = newType(modelPkg, "SubFoo");
        subFooRow.getTable("super").extend(true).addRef(fooRow);
        associate(modelPkg, subFooRow, barRow, true);
        model.assignRoleNames();
        Txn.mgr.commit();
        testFor(tester, model, "subFoo");
        testFor(tester, model, "bar3");
        Txn.mgr.begin();
        Table.Row subBarRow = newType(modelPkg, "SubBar");
        subBarRow.getTable("super").extend(true).addRef(barRow);
        associate(modelPkg, subFooRow, subBarRow, true);
        model.assignRoleNames();
        Txn.mgr.commit();
        testFor(tester, model, "subBar");
        testFor(tester, model, "subFoo1");
        Txn.mgr.begin();
        associate(modelPkg, fooRow, subBarRow, true);
        model.assignRoleNames();
        Txn.mgr.commit();
        testFor(tester, model, "subBar1");
        testFor(tester, model, "foo3");
    }

    private static void testFor(Tester tester, Model model, String partName) throws AccessException {
        Participant.Itr partItr = model.participantItr();
        while (partItr.next()) {
            String nextName = partItr.getName();
            if (partName.equals(nextName)) {
                tester.worked("Found \"" + partName + '"');
                return;
            }
        }
        tester.failed("Couldn't find participant \"" + partName + '"');
    }

    private static Table.Row newType(PkgTable modelPkg, String name) throws AccessException {
        Table entityExtent = modelPkg.pkgRow("Entity").getEntry();
        Table.Row baseRow = entityExtent.extend(true).addRow();
        baseRow.setString("name", name);
        newAttr(modelPkg, baseRow, "name", "string");
        return baseRow;
    }

    private static Table.Row newAttr(PkgTable modelPkg, Table.Row entity, String name, String type) throws AccessException {
        Table entityExtent = modelPkg.pkgRow("Entity").getEntry();
        Table attr = entity.getTable("owns");
        Table.Row attrRow = attr.extend(true).addRow();
        attrRow.setString("name", name);
        attrRow.extend("type").addRef(entityExtent.row(type));
        return attrRow;
    }

    private static int counter = 1;

    private static void associate(PkgTable modelPkg, Table.Row from, Table.Row to, boolean nav) throws AccessException {
        Table ascnExtent = modelPkg.getExtent("Association");
        Table.Row theAscn = ascnExtent.extend(true).addRow();
        theAscn.setString("name", "A" + (counter++));
        Table.Row fromPart = theAscn.getTable("parts").extend(true).addRow();
        fromPart.extend("type", true).addRef(from);
        Table.Row toPart = theAscn.getTable("parts").extend(true).addRow();
        toPart.extend("type", true).addRef(to);
    }

    public static void main(String[] argv) {
        Tester.run(ModelUtils.class);
    }
}
