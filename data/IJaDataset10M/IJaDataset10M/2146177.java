package eric;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import rene.zirkel.ZirkelFrame;
import rene.zirkel.macro.Macro;
import rene.zirkel.objects.ConstructionObject;

public class JMacrosInspector {

    ZirkelFrame ZF;

    JZirkelFrame JZF;

    JPalette freeJP;

    Macro m;

    int PW = 310;

    JDefaultMutableTreeNode node;

    IContent content;

    public JMacrosInspector(final ZirkelFrame zf, final JZirkelFrame jzf) {
        ZF = zf;
        JZF = jzf;
        freeJP = new JPalette(ZF, JZF, false);
        freeJP.setFocusableWindowState(true);
        freeJP.setFocusable(false);
        final JPaletteZone freeJPZ = new JPaletteZone(ZF, null, freeJP, getLocal("mi.pal.name"), "macrosproperties", PW);
        freeJPZ.setFocusable(false);
        freeJPZ.ZoneContent.setFocusable(false);
        content = new IContent(this);
        freeJPZ.ZoneContent.removeAll();
        freeJPZ.ZoneContent.add(content);
        freeJP.addWindowListener(new WindowAdapter() {

            @Override
            public void windowDeactivated(final WindowEvent e) {
                if (!(freeJP.isVisible())) {
                    content.changemacro();
                    node = null;
                    m = null;
                }
            }
        });
        freeJP.addzone(freeJPZ);
        freeJP.pack();
    }

    public String getLocal(final String s) {
        return JZF.Strs.getString(s);
    }

    public void setStandardLocation() {
        freeJP.setLocation(JZF.getLocation().x + JZF.ZContent.leftpanelwidth + 10, JZF.getLocation().y + 100);
    }

    public void clearPalette() {
        if (m != null) content.changemacro();
        node = null;
        m = null;
        content.clearfields();
    }

    public void setMacro(final JDefaultMutableTreeNode mynode) {
        if (m != null) content.changemacro();
        node = mynode;
        m = node.m;
        content.fillfields();
    }

    public void setVisible(final boolean bool) {
        freeJP.setVisible(bool);
    }

    public class IContent extends JPanel {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        JMacrosInspector JMI;

        JLabel name;

        JTextArea comment;

        JMacrosProperties props;

        JCheckBox hideDuplicates;

        private JPanel margin(final int w) {
            final JPanel mypan = new JPanel();
            fixsize(mypan, w, 1);
            mypan.setOpaque(false);
            return mypan;
        }

        public IContent(final JMacrosInspector jmi) {
            JMI = jmi;
            setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
            this.setFocusable(false);
            name = new JLabel();
            comment = new JTextArea();
            props = new JMacrosProperties(JMI);
            hideDuplicates = new JCheckBox(getLocal("mi.hideduplicates"));
            newnameline();
            this.add(new mySep(1));
            newcommentline();
            this.add(new mySep(1));
            newproperties();
            this.add(new mySep(1));
            newhideproperties();
            this.add(new mySep(1));
            newcontrolline();
        }

        private void fixsize(final JComponent cp, final int w, final int h) {
            final Dimension d = new Dimension(w, h);
            cp.setMaximumSize(d);
            cp.setMinimumSize(d);
            cp.setPreferredSize(d);
            cp.setSize(d);
        }

        public void newnameline() {
            final JPanel rub = new myRub();
            final JPanel myline1 = new ContentLine(25);
            fixsize(name, PW - 10, 18);
            myline1.add(margin(5));
            myline1.add(name);
            rub.add(myline1);
            this.add(rub);
        }

        public void newcommentline() {
            final JPanel rub = new myRub();
            final JScrollPane jScroll = new JScrollPane();
            final JPanel myline1 = new ContentLine(22);
            final JLabel namelabel = new JLabel(getLocal("mi.comment"));
            fixsize(namelabel, PW - 10, 14);
            myline1.add(margin(5));
            myline1.add(namelabel);
            final JPanel myline2 = new ContentLine(100);
            comment.setLineWrap(true);
            jScroll.setViewportView(comment);
            jScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            fixsize(jScroll, PW - 10, 80);
            myline2.add(margin(5));
            myline2.add(jScroll);
            rub.add(myline1);
            rub.add(myline2);
            this.add(rub);
        }

        public void newhideproperties() {
            final JPanel rub = new myRub();
            rub.setLayout(new javax.swing.BoxLayout(rub, javax.swing.BoxLayout.Y_AXIS));
            final JPanel myline1 = new ContentLine(22);
            final JLabel namelabel = new JLabel(getLocal("mi.hideproperties"));
            fixsize(namelabel, PW - 10, 14);
            myline1.add(margin(5));
            myline1.add(namelabel);
            final JPanel mylineC3 = new ContentLine(27);
            hideDuplicates.setOpaque(false);
            mylineC3.add(margin(20));
            mylineC3.add(hideDuplicates);
            rub.add(myline1);
            rub.add(mylineC3);
            this.add(rub);
        }

        public void newcontrolline() {
            final JPanel rub = new myRub();
            final JPanel myline = new ContentLine(25);
            final JButton applybtn = new JButton("Apply");
            applybtn.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    changemacro();
                    fillfields();
                }
            });
            fixsize(applybtn, 90, 18);
            applybtn.setFont(new Font("System", Font.BOLD, 11));
            final JPanel sep = new JPanel();
            sep.setOpaque(false);
            myline.add(sep);
            myline.add(applybtn);
            myline.add(margin(5));
            rub.add(myline);
            this.add(rub);
        }

        public void newproperties() {
            final JPanel rub = new myRub();
            final JPanel myline1 = new ContentLine(22);
            final JLabel namelabel = new JLabel(getLocal("mi.properties"));
            myline1.add(margin(5));
            myline1.add(namelabel);
            fixsize(namelabel, PW - 10, 14);
            final JPanel myline2 = new ContentLine(130);
            fixsize(props, PW - 10, 100);
            myline2.add(margin(5));
            myline2.add(props);
            rub.add(myline1);
            rub.add(myline2);
            this.add(rub);
        }

        /*************************************************
		 * this is the tricky method : it reads the inspector changes and then
		 * store the new values in the macro m. A macro contains two types of
		 * parameters : 1) normal parameters (the one you shows at the first
		 * step of macro's creation ). They are inside the m.Params array for
		 * ConstructionObjects and m.Prompts array for prompts 2) numerical
		 * input parameters (it's possible to make macros with numerical inputs
		 * ). Name of Objects are in the PromptFor array and prompts in the
		 * PromptName array
		 *************************************************/
        public void changemacro() {
            ConstructionObject[] params;
            final Vector newparams = new Vector();
            final Vector newprompts = new Vector();
            final Vector newpromptFor = new Vector();
            final Vector newpromptName = new Vector();
            props.stopCellEditing();
            if (m == null) return;
            if (isError()) return;
            m.setComment(comment.getText());
            params = m.getParams();
            for (int i = 0; i < params.length; i++) {
                params[i].setName(props.getOName(i));
                if (props.getOAsk(i)) {
                    newpromptFor.add(props.getOName(i));
                    newpromptName.add(props.getOPrompt(i));
                    params[i].setHidden(true);
                    params[i].clearParameter();
                } else {
                    newparams.add(params[i]);
                    if (props.getOFix(i)) {
                        newprompts.add("=" + props.getOName(i));
                    } else {
                        newprompts.add(props.getOPrompt(i));
                    }
                }
            }
            int rang = 0;
            for (int i = params.length; i < props.getRowCount(); i++) {
                ConstructionObject E = null;
                for (int j = 0; j < m.V.size(); j++) {
                    if (((ConstructionObject) m.V.get(j)).getName().equals(m.PromptFor[i - params.length])) {
                        E = (ConstructionObject) m.V.get(j);
                        break;
                    }
                }
                E.setName(props.getOName(i));
                if (props.getOAsk(i)) {
                    newpromptFor.add(props.getOName(i));
                    newpromptName.add(props.getOPrompt(i));
                } else {
                    newparams.add(rang, E);
                    if (props.getOFix(i)) {
                        newprompts.add(rang, "=" + props.getOName(i));
                    } else {
                        newprompts.add(rang, props.getOPrompt(i));
                    }
                    rang++;
                }
            }
            int ln = newparams.size();
            m.Params = new ConstructionObject[ln];
            m.Prompts = new String[ln];
            m.LastParams = new String[ln];
            for (int i = 0; i < ln; i++) {
                m.Params[i] = (ConstructionObject) newparams.get(i);
                m.Prompts[i] = (String) newprompts.get(i);
                m.LastParams[i] = null;
                m.Params[i].setHidden(false);
                m.Params[i].setMainParameter();
                m.Params[i].setParameter();
            }
            ln = newpromptFor.size();
            m.PromptFor = new String[ln];
            m.PromptName = new String[ln];
            for (int i = 0; i < ln; i++) {
                m.PromptFor[i] = (String) newpromptFor.get(i);
                m.PromptName[i] = (String) newpromptName.get(i);
            }
            m.hideDuplicates(hideDuplicates.isSelected());
        }

        public void fixObject(final int i, final boolean fix) {
            final String newprompt = (fix) ? "" : props.getOName(i);
            props.setOPrompt(newprompt, i);
            if (fix) props.setOAsk(false, i);
        }

        public void askObject(final int i, final boolean ask) {
            if ((ask) && props.getOFix(i)) {
                props.setOFix(false, i);
                fixObject(i, false);
            }
        }

        public void clearfields() {
            name.setText(getLocal("mi.name"));
            comment.setText("");
            props.removeAllRows();
            hideDuplicates.setSelected(false);
        }

        public boolean isError() {
            final boolean isErr = false;
            final int ln = props.getRowCount();
            boolean err = true;
            for (int i = 0; i < ln; i++) {
                err = (err) && ((props.getOFix(i)) || (props.getOAsk(i)));
            }
            if (err) {
                JOptionPane.showMessageDialog(null, getLocal("mi.error.initial"));
                return true;
            }
            return isErr;
        }

        public void fillfields() {
            ConstructionObject[] params;
            String[] prompts;
            if (m == null) return;
            name.setText(getLocal("mi.name") + " " + (String) node.getUserObject());
            comment.setText(m.Comment);
            props.removeAllRows();
            params = m.getParams();
            prompts = m.getPrompts();
            String pr = "";
            for (int i = 0; i < params.length; i++) {
                pr = "=" + params[i].getName();
                String tpe = "";
                try {
                    tpe = JZF.Strs.getString(params[i].getClass().getName());
                } catch (final Exception e) {
                    tpe = params[i].getClass().getName();
                }
                final boolean withask = params[i].getClass().getName().endsWith("ExpressionObject");
                if (withask) {
                    props.addRow(tpe, params[i].getName(), prompts[i], prompts[i].equals(pr), false);
                } else {
                    props.addRow(tpe, params[i].getName(), prompts[i], prompts[i].equals(pr));
                }
            }
            for (int i = 0; i < m.PromptFor.length; i++) {
                final String tpe = JZF.Strs.getString("rene.zirkel.objects.ExpressionObject");
                props.addRow(tpe, m.PromptFor[i], m.PromptName[i], false, true);
            }
            hideDuplicates.setSelected(m.hideDuplicates());
        }

        class myRub extends javax.swing.JPanel {

            /**
			 * 
			 */
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(final java.awt.Graphics g) {
                super.paintComponent(g);
                final java.awt.Dimension d = this.getSize();
                g.drawImage(JZF.JZT.getImage("palbackground2.gif"), 0, 0, d.width, d.height, this);
            }

            public myRub() {
                this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
                this.setAlignmentX(0F);
            }
        }

        class ContentLine extends javax.swing.JPanel {

            /**
			 * 
			 */
            private static final long serialVersionUID = 1L;

            public ContentLine(final int height) {
                this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
                this.setAlignmentX(0F);
                this.setMaximumSize(new java.awt.Dimension(PW, height));
                this.setMinimumSize(new java.awt.Dimension(PW, height));
                this.setPreferredSize(new java.awt.Dimension(PW, height));
                this.setSize(PW, height);
                this.setOpaque(false);
            }
        }

        class mySep extends javax.swing.JPanel {

            /**
			 * 
			 */
            private static final long serialVersionUID = 1L;

            public mySep(final int height) {
                this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
                this.setAlignmentX(0F);
                this.setMaximumSize(new java.awt.Dimension(PW, height));
                this.setMinimumSize(new java.awt.Dimension(PW, height));
                this.setPreferredSize(new java.awt.Dimension(PW, height));
                this.setSize(PW, height);
                this.setBackground(new Color(200, 200, 200));
            }
        }
    }
}
