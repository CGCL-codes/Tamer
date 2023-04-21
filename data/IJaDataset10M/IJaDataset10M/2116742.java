package remote.gui.treeorderings;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import remote.gui.util.MoteControlRow;
import remote.gui.util.TableRowOrdering;
import remote.gui.ReMote;
import remote.gui.RenderableTreeNode;
import remote.gui.IconResources;
import remote.service.motedata.client.TableRow;

public class OrderingsAllMotesView {

    private static class MoteNodeRenderer extends RenderableTreeNode {

        JPanel mote = new JPanel();

        protected MoteNodeRenderer(Comparable key, TableRow row, boolean numeric) {
            super(key, numeric);
            mote.setBackground(new Color(0, true));
            ImageIcon moteicon = IconResources.MOTE;
            ImageIcon usageicon = null;
            try {
                String platform = row.get("platform").toString();
                if (platform.equalsIgnoreCase("micaz")) {
                    moteicon = IconResources.MOTE_MICAZ;
                } else if (platform.equalsIgnoreCase("tmotesky")) {
                    moteicon = IconResources.MOTE_TMOTE_SKY;
                } else if (platform.equalsIgnoreCase("dig528-2")) {
                    moteicon = IconResources.MOTE_DIG528_2;
                }
                String availability = row.get("mote_usage").toString();
                if (availability.equals("available")) {
                    usageicon = null;
                } else if (availability.equals("occupied")) {
                    usageicon = IconResources.MOTE_OCCUPIED;
                } else if (availability.equals("controlled")) {
                    usageicon = IconResources.MOTE_CONTROLLED;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JLabel label = new JLabel(moteicon);
            label.setText(key.toString());
            mote.setToolTipText(getToolTip(row));
            mote.add(label);
            if (usageicon != null) {
                mote.add(new JLabel(usageicon));
            }
        }

        protected static String getToolTip(TableRow row) {
            try {
                return row.get("netaddress") + " - " + row.get("platform") + " (" + row.get("macaddress") + ") @ " + row.get("site");
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        public Component getRenderable() {
            return mote;
        }
    }

    private static class RenderableOrdering extends SimpleTableRowOrdering {

        boolean numeric;

        public RenderableOrdering(String key, String name, boolean total, boolean numeric) {
            super(key, name, total);
            this.numeric = numeric;
        }

        public Comparable getKey(TableRow row) {
            return new MoteNodeRenderer(super.getKey(row), row, numeric);
        }
    }

    ;

    public static TableRowOrdering BY_USAGE = new SimpleTableRowOrdering("mote_usage", "Usage", false);

    public static TableRowOrdering BY_NET = new RenderableOrdering("netaddress", "NET", true, true);

    public static TableRowOrdering BY_MAC = new RenderableOrdering("macaddress", "MAC", true, true);

    /**public static TableRowOrdering BY_MOTE_ID
	= new RenderableOrdering("mote_id","mote_id",true);**/
    public static TableRowOrdering[] orderings = { BY_NET, BY_MAC, BY_USAGE };
}
