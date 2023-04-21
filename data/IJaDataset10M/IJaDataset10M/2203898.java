package com.monkygames.sc2bob.io;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import com.monkygames.sc2bob.objects.*;

/**
 * Exports to the YABOT format.
 * http://www.sc2mapster.com/maps/yabot/
 * @version 1.0
 */
public class YABOTImportExporter {

    private YABOTEncodingManager manager;

    public YABOTImportExporter(YABOTEncodingManager manager) {
        this.manager = manager;
    }

    /**
     * Exports the build order to the specified file.
     * @param buildOrder the build order to export.
     * @param file the file for the yabot format to be saved in.
     * @return true on success and false otherwise.
     **/
    public boolean export(BuildOrder buildOrder, File file) {
        Vector<Vector> supplyV = buildOrder.getActionsBySupply();
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            String out = "100 [i] " + buildOrder.name + " | " + manager.getRace(buildOrder.race, buildOrder.opponentRace) + " | " + buildOrder.credit;
            out += " | " + buildOrder.summary + " [/i] [s] ";
            for (int i = 0; i < supplyV.size(); i++) {
                Vector<BuildAction> v = supplyV.elementAt(i);
                for (int j = 0; j < v.size(); j++) {
                    BuildAction ba = v.elementAt(j);
                    String cancelFlag = "";
                    if (ba.isCanceled) {
                        cancelFlag = " 1 ";
                    } else {
                        cancelFlag = " 0 ";
                    }
                    String note = "";
                    if (ba.note != null && ba.note.length() > 0) {
                        note = ba.note + " ";
                    }
                    out += ba.supplyState + " 0 0 0 1 " + manager.getType(ba.base.name) + " " + manager.getItem(ba.base.name) + cancelFlag + note;
                    if (j != v.size() - 1) {
                        out += "|";
                    }
                }
                if (i != supplyV.size() - 1) {
                    out += "|";
                }
            }
            out += "[/s]";
            pw.write(out);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Imports from the YABOT format.
     * @param file the file to import from.
     * @return the build order and null on error.
     **/
    public BuildOrder importYABOT(File file) {
        BuildOrder bo = new BuildOrder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String data = br.readLine();
            String startC = "[i]";
            String endC = "[/i]";
            int start, end;
            start = end = 0;
            for (int i = 0; i < data.length() - startC.length(); i++) {
                if (data.substring(i, i + startC.length()).equals(startC)) {
                    start = i + startC.length();
                } else if (data.substring(i, i + endC.length()).equals(endC)) {
                    end = i;
                    break;
                }
            }
            String info = data.substring(start, end);
            String actions = data.substring(end + endC.length() + 4, data.length() - 4);
            parseBuildInfo(info, bo);
            parseBuildActions(actions, bo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bo;
    }

    /**
     * Parses the build information and populates the build order with the information.
     * @param info contains a string from the yabot file that contains the build information.
     * @param bo to be populated.
     **/
    private void parseBuildInfo(String info, BuildOrder bo) {
        StringTokenizer stok = new StringTokenizer(info, "|", false);
        bo.name = stok.nextToken().trim();
        int matchup = Integer.parseInt(stok.nextToken().trim());
        bo.race = manager.findBuildRace(matchup);
        bo.opponentRace = manager.findOpponentRace(matchup);
        bo.credit = stok.nextToken().trim();
        bo.summary = stok.nextToken().trim();
    }

    private void parseBuildActions(String actions, BuildOrder bo) {
        StringTokenizer stok = new StringTokenizer(actions, "|", false);
        while (stok.hasMoreTokens()) {
            bo.buildActionsV.add(parseBuildAction(bo.race, stok.nextToken().trim()));
        }
    }

    private BuildAction parseBuildAction(int race, String action) {
        BuildAction ba = new BuildAction();
        StringTokenizer stok = new StringTokenizer(action, " ", false);
        ba.supplyState = Integer.parseInt(stok.nextToken().trim());
        stok.nextToken();
        stok.nextToken();
        stok.nextToken();
        int quantity = Integer.parseInt(stok.nextToken().trim());
        int type = Integer.parseInt(stok.nextToken().trim());
        int item = Integer.parseInt(stok.nextToken().trim());
        int cancelFlag = Integer.parseInt(stok.nextToken().trim());
        if (stok.hasMoreTokens()) {
            ba.note = stok.nextToken();
        }
        if (cancelFlag == 0) {
            ba.isCanceled = false;
        } else {
            ba.isCanceled = true;
        }
        ba.base = manager.getBase(race, type, item);
        return ba;
    }

    public static void main(String[] args) {
        YABOTEncodingManager manager = new YABOTEncodingManager(new ObjectManager());
        YABOTImportExporter test = new YABOTImportExporter(manager);
        BuildOrder bo = test.importYABOT(new File("docs/import_test1.yabot"));
    }
}
