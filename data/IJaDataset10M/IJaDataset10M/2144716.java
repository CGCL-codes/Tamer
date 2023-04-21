package org.objectwiz.fxclient.utils;

import fr.helmet.javafx.table.ObjectToColumnsParser;

/**
 * Utilities for legacy compatibility.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class LegacyUtils {

    public static ObjectToColumnsParser convertOTCP(final org.objectwiz.ui.table.ObjectToColumnsParser parser) {
        return new ObjectToColumnsParser() {

            @Override
            public String[] getColumnIds() {
                return parser.getColumnIds();
            }

            @Override
            public String[] getColumnNames() {
                return parser.getColumnNames();
            }

            @Override
            public Object[] getColumns(Object obj) {
                return parser.getColumns(obj);
            }
        };
    }
}
