package muse.external.solver;

import java.util.Arrays;
import java.util.List;
import muse.external.AbstractExternalModuleBridge;
import muse.external.ExternalException;
import muse.external.console.DefaultCommandLineModuleBridge;

/**
 * 
 *
 * @author Korchak
 */
public class Test {

    public static void main(String[] args) {
        final int[][] table = new int[][] { { 0, 1, 1, 0, 1, 0 }, { 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0 } };
        final Object[] array = new Object[] { new DefaultCommandLineModuleBridge(), new AbstractExternalModuleBridge() {

            public void abort() {
            }

            public void execute() throws ExternalException {
            }

            public int getID() {
                return -1;
            }
        }, new DefaultCommandLineModuleBridge(), new AbstractExternalModuleBridge() {

            public void abort() {
            }

            public void execute() throws ExternalException {
            }

            public int getID() {
                return 1;
            }
        }, new DefaultCommandLineModuleBridge(), new AbstractExternalModuleBridge() {

            public void abort() {
            }

            public void execute() throws ExternalException {
            }

            public int getID() {
                return 1;
            }
        } };
        BlockSequenceCreator creator = new AbstractBlockSequenceCreator() {

            protected GraphTable createGraphTable() {
                return new AbstractBlockSequenceCreator.GraphTable(table);
            }

            public List getBlockList() {
                return Arrays.asList(array);
            }
        };
        List list = creator.sequence();
        System.out.println(list);
    }
}
