package org.epoline.phoenix.indexing.shared;

import org.epoline.phoenix.common.shared.AbstractTableDescriptor;
import org.epoline.phoenix.common.shared.Item;
import org.epoline.phoenix.common.shared.TableDescriptor;

public class ItemCompositionDossierKey extends Item implements Cloneable {

    private final String name;

    private final String type;

    public ItemCompositionDossierKey(String type, String name) {
        super();
        if (!isValidName(name) || !isValidType(type)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.type = type;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        ItemCompositionDossierKey other = (ItemCompositionDossierKey) obj;
        return other.getName().equals(this.getName()) && other.getType().equals(this.getType());
    }

    public String getName() {
        return name;
    }

    public TableDescriptor getTableDescriptor() {
        return new AbstractTableDescriptor() {

            private String[] COLUMNS = { "Type", "Name" };

            public Object getField(Item item, int column) {
                ItemCompositionDossierKey key = (ItemCompositionDossierKey) item;
                if (key != null) {
                    switch(column) {
                        case 0:
                            return key.getType();
                        case 1:
                            return key.getName();
                    }
                }
                return null;
            }

            public Class getColumnClass(int column) {
                switch(column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                }
                return null;
            }

            public String[] getColumnNames() {
                return COLUMNS;
            }
        };
    }

    public String getType() {
        return type;
    }

    public static boolean isValidName(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        return name.trim().length() > 0;
    }

    public static boolean isValidType(String type) {
        if (type == null) {
            throw new NullPointerException();
        }
        return type.trim().length() > 0;
    }
}
