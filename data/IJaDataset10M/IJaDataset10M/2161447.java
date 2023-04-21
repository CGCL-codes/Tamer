package oxdoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import oxdoc.entities.OxClass;
import oxdoc.entities.OxEntity;
import oxdoc.entities.OxEnumElement;
import oxdoc.entities.OxField;
import oxdoc.entities.OxMethod;
import oxdoc.html.Table;

class SymbolIndex {

    private OxProject project = null;

    private OxDoc oxdoc = null;

    private ClassTree classTree = null;

    private class IndexEntry {

        String text = "";

        String type = "";

        OxEntity entity = null;

        ArrayList owningClassMembers = new ArrayList();

        IndexEntry(String text, String type, OxEntity entity) {
            this.text = text;
            this.type = type;
            this.entity = entity;
        }
    }

    Hashtable entries = null;

    public SymbolIndex(OxDoc oxdoc, ClassTree classTree) {
        this.oxdoc = oxdoc;
        project = oxdoc.project;
        this.classTree = classTree;
        constructIndex();
    }

    private void constructIndex() {
        entries = new Hashtable();
        ArrayList symbols = project.symbolsByDisplayName();
        for (int i = 0; i < symbols.size(); i++) {
            OxEntity entity = (OxEntity) symbols.get(i);
            if ((!oxdoc.config.ShowInternals) && entity.isInternal()) continue;
            if (entity instanceof OxClass) addSingletonEntry(entity, "Class"); else if ((entity instanceof OxField) && (entity.parentClass() == null)) addSingletonEntry(entity, "Global variable"); else if (entity instanceof OxField) addGroupedEntry(entity, entity, "Field"); else if (entity instanceof OxEnumElement) {
                OxEnumElement element = (OxEnumElement) entity;
                addSingletonEntry(entity, "Element of enumeration " + project.linkToEntity(element.parentEnum()));
            } else if ((entity instanceof OxMethod) && (entity.parentClass() == null)) addSingletonEntry(entity, "Global function"); else if (entity instanceof OxMethod) {
                OxMethod method = (OxMethod) entity;
                OxClass parentClass = method.parentClass();
                String type = "Method";
                if (method.name().compareTo(parentClass.name()) == 0) type = "Constructor"; else if (method.name().compareTo("~" + parentClass.name()) == 0) type = "Destructor";
                OxClass ancestorClass = parentClass;
                while ((ancestorClass.superClass() != null) && (ancestorClass.superClass().methodByName(method.name()) != null)) ancestorClass = ancestorClass.superClass();
                addGroupedEntry(ancestorClass.methodByName(method.name()), entity, type);
            }
        }
    }

    private IndexEntry addSingletonEntry(OxEntity entity, String type) {
        IndexEntry entry = new IndexEntry(entity.name(), type, entity);
        entries.put(entity, entry);
        return entry;
    }

    private IndexEntry addGroupedEntry(OxEntity ancestorEntity, OxEntity entity, String type) {
        IndexEntry entry = (IndexEntry) entries.get(ancestorEntity);
        if (entry == null) entry = new IndexEntry(ancestorEntity.name(), type, entity);
        entry.owningClassMembers.add(entity);
        entries.put(ancestorEntity, entry);
        return entry;
    }

    private void sortEntriesByName(ArrayList indexEntries) {
        Collections.sort(indexEntries, new Comparator() {

            public int compare(Object o1, Object o2) {
                IndexEntry e1 = (IndexEntry) o1;
                IndexEntry e2 = (IndexEntry) o2;
                return e1.text.toUpperCase().compareTo(e2.text.toUpperCase());
            }
        });
    }

    private void sortOwningClassMembers(ArrayList members) {
        Collections.sort(members, new Comparator() {

            public int compare(Object o1, Object o2) {
                OxClass e1 = ((OxEntity) o1).parentClass();
                OxClass e2 = ((OxEntity) o2).parentClass();
                int depth1 = classTree.getClassDepth(e1);
                int depth2 = classTree.getClassDepth(e2);
                if (depth1 != depth2) return depth2 - depth1;
                return e1.name().toUpperCase().compareTo(e2.name().toUpperCase());
            }
        });
    }

    public void write(OutputFile output) throws Exception {
        ArrayList indexEntries = new ArrayList();
        indexEntries.addAll(entries.values());
        sortEntriesByName(indexEntries);
        Table table = new Table(oxdoc);
        table.specs().cssClass = "index";
        table.specs().columnCssClasses.add("declaration");
        table.specs().columnCssClasses.add("description");
        for (int i = 0; i < indexEntries.size(); i++) {
            IndexEntry entry = (IndexEntry) indexEntries.get(i);
            OxEntity entity = entry.entity;
            String description = entry.type;
            if (entry.owningClassMembers.size() > 0) {
                sortOwningClassMembers(entry.owningClassMembers);
                description += " of ";
                for (int j = 0; j < entry.owningClassMembers.size(); j++) {
                    OxEntity memberEntity = (OxEntity) entry.owningClassMembers.get(j);
                    description += (j == 0 ? "" : ", ") + project.linkToEntity(memberEntity, memberEntity.parentClass().name());
                }
            }
            String[] row = { entity.smallIcon() + project.linkToEntity(entity), description };
            table.addRow(row);
        }
        output.writeln(table);
    }
}
