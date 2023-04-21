package net.sourceforge.entrainer.gui.laf;

import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Eliminates known broken LAFs from the distribution list.
 *     
 *  <br><br>Copyright (C) 2008  Burton Alexander<br><br>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.<br><br>
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.<br><br>
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.<br><br>
 *
 * @author burton
 *
 */
public class LAFDiscriminator {

    private static final String[] BROKEN_LAFS = { "Substance Business Black Steel", "Substance Business Blue Steel", "Substance Business", "Substance Office Blue 2007", "Substance Office Silver 2007" };

    public LAFDiscriminator() {
        super();
    }

    public static LookAndFeelInfo[] getWorkingLafs() {
        LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        List<LookAndFeelInfo> list = processInfos(infos);
        LookAndFeelInfo[] processed = new LookAndFeelInfo[list.size()];
        processed = list.toArray(processed);
        return processed;
    }

    private static List<LookAndFeelInfo> processInfos(LookAndFeelInfo[] infos) {
        List<LookAndFeelInfo> list = new ArrayList<LookAndFeelInfo>();
        for (LookAndFeelInfo info : infos) {
            if (!containsLaf(list, info) && !isBroken(info)) {
                list.add(info);
            }
        }
        return list;
    }

    private static boolean isBroken(LookAndFeelInfo info) {
        for (String broken : BROKEN_LAFS) {
            if (info.getName().equals(broken)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsLaf(List<LookAndFeelInfo> list, LookAndFeelInfo info) {
        for (LookAndFeelInfo i : list) {
            if (i.getName().equals(info.getName())) {
                return true;
            }
        }
        return false;
    }
}
