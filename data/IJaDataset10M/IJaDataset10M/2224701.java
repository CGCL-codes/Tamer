package com.ivan.game.eventcreater;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.ivan.game.itemcreater.ItemCreaterPanel;
import com.ivan.game.logger.GameLogger;
import com.ivan.game.mapcreater.MapCreaterFrame;
import com.ivan.game.unit.DatFilter;
import com.ivan.game.unit.NumberTester;

public class CreaterPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3781918923414002919L;

    public CreaterPanel() {
        this.setBackground(Color.CYAN);
        JLabel filenamelabel = new JLabel("文件名:");
        JLabel eventnamelabel = new JLabel("事件名:");
        JLabel eventinfolabel = new JLabel("事件描述:");
        JLabel needeventlabel = new JLabel("前续事件文件:");
        JLabel followeventlabel = new JLabel("后续事件文件");
        JLabel needitemlabel = new JLabel("需要物品文件:");
        JLabel eventtypelabel = new JLabel("事件类型:");
        JLabel enemynamelabel = new JLabel("怪物文件:");
        JLabel itemnamelabel = new JLabel("物品文件:");
        JLabel moneylabel = new JLabel("金钱数量");
        JLabel targetmaplabel = new JLabel("目标地图文件:");
        JLabel targetxlabel = new JLabel("坐标X:");
        JLabel targetylabel = new JLabel("坐标Y:");
        JLabel magiclabel = new JLabel("习得技能:");
        filenametext = new JTextField(10);
        eventnametext = new JTextField(10);
        eventinfotext = new JTextField(10);
        needeventtext = new JTextField("双击打开文件", 10);
        needeventtext.setEditable(false);
        needeventtext.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("./data/event/"));
                    chooser.setFileFilter(new DatFilter());
                    int result = chooser.showOpenDialog(CreaterPanel.this);
                    if (result == 0) {
                        String filepath = chooser.getSelectedFile().getPath();
                        String path = filepath.substring(filepath.lastIndexOf("data"));
                        needeventtext.setText(path);
                        needevent = path;
                    }
                }
            }
        });
        needitemtext = new JTextField("双击打开文件", 10);
        needitemtext.setEditable(false);
        needitemtext.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("./data/item/"));
                    chooser.setFileFilter(new DatFilter());
                    int result = chooser.showOpenDialog(CreaterPanel.this);
                    if (result == 0) {
                        String filepath = chooser.getSelectedFile().getPath();
                        String path = filepath.substring(filepath.lastIndexOf("data"));
                        needitemtext.setText(path);
                        needitem = path;
                    }
                }
            }
        });
        followeventtext = new JTextField("双击打开文件", 10);
        followeventtext.setEditable(false);
        followeventtext.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("./data/event/"));
                    chooser.setFileFilter(new DatFilter());
                    int result = chooser.showOpenDialog(CreaterPanel.this);
                    if (result == 0) {
                        String filepath = chooser.getSelectedFile().getPath();
                        String path = filepath.substring(filepath.lastIndexOf("data"));
                        followeventtext.setText(path);
                        followevent = path;
                    }
                }
            }
        });
        eventtypecombo = new JComboBox();
        eventtypecombo.addItem("传送事件");
        eventtypecombo.addItem("战斗事件");
        eventtypecombo.addItem("物品事件");
        eventtypecombo.addItem("金钱事件");
        eventtypecombo.addItem("学习事件");
        enemynametext = new JTextField("双击打开文件", 10);
        enemynametext.setEditable(false);
        enemynametext.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("./data/enemy/"));
                    chooser.setFileFilter(new DatFilter());
                    int result = chooser.showOpenDialog(CreaterPanel.this);
                    if (result == 0) {
                        String filepath = chooser.getSelectedFile().getPath();
                        String path = filepath.substring(filepath.lastIndexOf("data"));
                        enemynametext.setText(path);
                        enemyname = path;
                    }
                }
            }
        });
        itemnametext = new JTextField("双击打开文件", 10);
        itemnametext.setEditable(false);
        itemnametext.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("./data/item/"));
                    chooser.setFileFilter(new DatFilter());
                    int result = chooser.showOpenDialog(CreaterPanel.this);
                    if (result == 0) {
                        String filepath = chooser.getSelectedFile().getPath();
                        String path = filepath.substring(filepath.lastIndexOf("data"));
                        itemnametext.setText(path);
                        itemname = path;
                    }
                }
            }
        });
        moneytext = new JTextField("0", 10);
        targetmaptext = new JTextField("双击打开文件", 10);
        targetmaptext.setEditable(false);
        targetmaptext.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("./data/map/"));
                    chooser.setFileFilter(new DatFilter());
                    int result = chooser.showOpenDialog(CreaterPanel.this);
                    if (result == 0) {
                        String filepath = chooser.getSelectedFile().getPath();
                        String path = filepath.substring(filepath.lastIndexOf("data"));
                        targetmaptext.setText(path);
                        targetmapname = path;
                    }
                }
            }
        });
        targetxtext = new JTextField(10);
        targetytext = new JTextField(10);
        magictext = new JTextField("双击打开文件");
        magictext.setEditable(false);
        magictext.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("./data/magic/"));
                    chooser.setFileFilter(new DatFilter());
                    int result = chooser.showOpenDialog(CreaterPanel.this);
                    if (result == 0) {
                        String filepath = chooser.getSelectedFile().getPath();
                        String path = filepath.substring(filepath.lastIndexOf("data"));
                        magictext.setText(path);
                        magic = path;
                    }
                }
            }
        });
        JButton savebutton = new JButton("保存事件");
        savebutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                save();
            }
        });
        JButton resetbutton = new JButton("重置数据");
        resetbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                init();
            }
        });
        JButton updatebutton = new JButton("更新数据");
        updatebutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                update();
            }
        });
        setLayout(new GridLayout(16, 2));
        add(filenamelabel);
        add(filenametext);
        add(eventnamelabel);
        add(eventnametext);
        add(eventinfolabel);
        add(eventinfotext);
        add(needeventlabel);
        add(needeventtext);
        add(followeventlabel);
        add(followeventtext);
        add(needitemlabel);
        add(needitemtext);
        add(eventtypelabel);
        add(eventtypecombo);
        add(enemynamelabel);
        add(enemynametext);
        add(itemnamelabel);
        add(itemnametext);
        add(moneylabel);
        add(moneytext);
        add(targetmaplabel);
        add(targetmaptext);
        add(targetxlabel);
        add(targetxtext);
        add(targetylabel);
        add(targetytext);
        add(magiclabel);
        add(magictext);
        add(savebutton);
        add(resetbutton);
    }

    public void init() {
        track("初始化...");
        openfile = null;
        filename = "";
        eventname = "";
        eventinfo = "";
        needevent = "";
        followevent = "";
        needitem = "";
        eventtype = "";
        enemyname = "";
        itemname = "";
        targetmapname = "";
        targetx = "";
        targety = "";
        money = "0";
        magic = "";
        filenametext.setText(filename);
        eventnametext.setText(eventname);
        eventinfotext.setText(eventinfo);
        needeventtext.setText("双击打开文件");
        needitemtext.setText("双击打开文件");
        followeventtext.setText("双击打开文件");
        enemynametext.setText("双击打开文件");
        itemnametext.setText("双击打开文件");
        targetmaptext.setText("双击打开文件");
        targetxtext.setText(targetx);
        targetytext.setText(targety);
        eventtypecombo.setSelectedIndex(0);
        moneytext.setText(money);
        magictext.setText("双击打开文件");
    }

    public void newFile() {
        track("等待建立新文件...");
        int result = JOptionPane.showConfirmDialog(CreaterPanel.this, "是否保存当前事件?", "等待确认", JOptionPane.YES_NO_CANCEL_OPTION);
        if (result == 0) {
            save();
            init();
        } else if (result == 1) {
            init();
        } else if (result == 2) {
            return;
        }
    }

    public void readFile(File f) {
        track("读取文件...");
        init();
        money = "";
        filename = f.getName();
        openfile = new File(f.getPath());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), "GBK"));
            filename = in.readLine();
            eventname = in.readLine();
            eventinfo = in.readLine();
            needevent = in.readLine();
            followevent = in.readLine();
            needitem = in.readLine();
            eventtype = in.readLine();
            enemyname = in.readLine();
            itemname = in.readLine();
            targetmapname = in.readLine();
            targetx = in.readLine();
            targety = in.readLine();
            money = in.readLine();
            magic = in.readLine();
            in.close();
            update();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean check() {
        track("检测数据...");
        String errormsg = "";
        boolean error = false;
        filename = filenametext.getText();
        eventname = eventnametext.getText();
        eventinfo = eventinfotext.getText();
        needevent = needeventtext.getText();
        followevent = followeventtext.getText();
        needitem = needitemtext.getText();
        enemyname = enemynametext.getText();
        itemname = itemnametext.getText();
        targetmapname = targetmaptext.getText();
        targetx = targetxtext.getText();
        targety = targetytext.getText();
        eventtype = (String) eventtypecombo.getSelectedItem();
        money = moneytext.getText();
        magic = magictext.getText();
        if (eventinfo.length() == 0) {
            eventinfo = "发生" + eventname + "事件.";
        }
        if (needevent.equals(new String("双击打开文件"))) needevent = "data/default.dat";
        if (followevent.equals(new String("双击打开文件"))) followevent = "data/default.dat";
        if (needitem.equals(new String("双击打开文件"))) needitem = "data/default.dat";
        if (itemname.equals(new String("双击打开文件"))) itemname = "data/default.dat";
        if (enemyname.equals(new String("双击打开文件"))) enemyname = "data/default.dat";
        if (targetmapname.equals(new String("双击打开文件"))) targetmapname = "data/default.dat";
        if (magic.equals("双击打开文件")) magic = "data/default.dat";
        if (money.length() == 0) money = "0";
        if (targetx.length() == 0) targetx = "0";
        if (targety.length() == 0) targety = "0";
        NumberTester tester = new NumberTester();
        if (filename.length() == 0) {
            errormsg += "请输入文件名\n";
            error = true;
        }
        if (filename.length() > 0 && !filename.endsWith(".dat")) {
            errormsg += "文件名格式不对(.dat)\n";
            error = true;
        }
        if (eventname.length() == 0) {
            errormsg += "请输入事件名\n";
            error = true;
        }
        if (needevent.length() != 0 && !needevent.endsWith(".dat")) {
            errormsg += "前续事件格式不对(.dat)\n";
            error = true;
        }
        if (followevent.length() != 0 && !followevent.endsWith(".dat")) {
            errormsg += "后续事件格式不对(.dat)\n";
            error = true;
        }
        if (magic.length() != 0 && !magic.endsWith(".dat")) {
            errormsg += "技能文件格式不对(.dat)\n";
            error = true;
        }
        if (eventtype.equals("物品事件") && itemname.equals("default.dat")) {
            errormsg += "物品事件必须有物品\n";
            error = true;
        }
        if (eventtype.equals("传送事件") && targetmapname.equals("default.dat")) {
            errormsg += "传送事件必须有目标地图\n";
            error = true;
        }
        if (eventtype.equals("战斗事件") && enemyname.equals("default.dat")) {
            errormsg += "战斗事件必须有敌人\n";
            error = true;
        }
        if (eventtype.equals("金钱事件")) {
            if (!tester.test(money)) {
                errormsg += "金钱事件必须指定金钱数量\n";
                error = true;
            }
        }
        if (eventtype.equals("学习事件") && magic.equals("default.dat")) {
            errormsg += "学习事件必须有技能\n";
            error = true;
        }
        if (needitem.length() != 0 && !needitem.endsWith(".dat")) {
            errormsg += "需要物品文件格式不对(.dat)\n";
            error = true;
        }
        if (itemname.length() != 0 && !itemname.endsWith(".dat")) {
            errormsg += "物品文件格式不对(.dat)\n";
            error = true;
        }
        if (enemyname.length() != 0 && !enemyname.endsWith(".dat")) {
            errormsg += "怪物文件格式不对(.dat)\n";
            error = true;
        }
        if (!targetmapname.equals("data/default.dat")) {
            if (!targetmapname.endsWith(".dat")) {
                errormsg += "地图格式不对(.dat)\n";
                error = true;
            }
            if (targetx.length() == 0 || targety.length() == 0) {
                errormsg += "请输入传送坐标\n";
                error = true;
            }
            if (!tester.test(targetx) || !tester.test(targety)) {
                errormsg += "坐标请输入整数\n";
                error = true;
            }
        }
        if (error) {
            JOptionPane.showMessageDialog(CreaterPanel.this, errormsg, "错误", JOptionPane.ERROR_MESSAGE);
            return error;
        }
        return error;
    }

    public void save() {
        if (check()) {
            return;
        }
        track("保存文件...");
        String saveData = "";
        savefile = new File("data/event/" + filename);
        int save = 0;
        if (savefile.exists()) {
            save = JOptionPane.showConfirmDialog(CreaterPanel.this, "此事件已经存在,是否覆盖?", "等待确认", JOptionPane.YES_NO_CANCEL_OPTION);
        }
        if (save == 0) {
            saveData = filename + "\n" + eventname + "\n" + eventinfo + "\n" + needevent + "\n" + followevent + "\n" + needitem + "\n" + eventtype + "\n" + enemyname + "\n" + itemname + "\n" + targetmapname + "\n" + targetx + "\n" + targety + "\n" + money + "\n" + magic + "\n" + "                                                   #\n";
            try {
                FileOutputStream out = new FileOutputStream(savefile);
                out.write(saveData.getBytes("utf-8"), 0, saveData.length());
                out.close();
                JOptionPane.showMessageDialog(CreaterPanel.this, "文件保存成功", "保存", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        track("更新控件...");
        filenametext.setText(filename);
        eventnametext.setText(eventname);
        eventinfotext.setText(eventinfo);
        if (needevent.length() > 0) needeventtext.setText(needevent); else needeventtext.setText("双击打开文件");
        if (followevent.length() > 0) followeventtext.setText(followevent); else followeventtext.setText("双击打开文件");
        if (needitem.length() > 0) needitemtext.setText(needitem); else needitemtext.setText("双击打开文件");
        if (itemname.length() > 0) itemnametext.setText(itemname); else itemnametext.setText("双击打开文件");
        if (enemyname.length() > 0) enemynametext.setText(enemyname); else enemynametext.setText("双击打开文件");
        if (targetmapname.length() > 0) targetmaptext.setText(targetmapname); else targetmaptext.setText("双击打开文件");
        magictext.setText(magic);
        targetxtext.setText(targetx);
        targetytext.setText(targety);
        if (eventtype.equals("传送事件")) eventtypecombo.setSelectedIndex(0); else if (eventtype.equals("战斗事件")) eventtypecombo.setSelectedIndex(1); else if (eventtype.equals("物品事件")) eventtypecombo.setSelectedIndex(2); else if (eventtype.equals("金钱事件")) eventtypecombo.setSelectedIndex(3); else if (eventtype.equals("学习事件")) eventtypecombo.setSelectedIndex(4);
        moneytext.setText(money);
    }

    public void track(String s) {
        GameLogger.logger.info("EventCreater|" + s);
    }

    private String filename;

    private String eventname;

    private String eventinfo;

    private String needevent;

    private String followevent;

    private String needitem;

    private String eventtype;

    private String enemyname;

    private String itemname;

    private String targetmapname;

    private String targetx;

    private String targety;

    private String money;

    private String magic;

    JTextField filenametext;

    JTextField eventnametext;

    JTextField eventinfotext;

    JTextField needeventtext;

    JTextField followeventtext;

    JTextField needitemtext;

    JTextField enemynametext;

    JTextField itemnametext;

    JTextField targetmaptext;

    JTextField targetxtext;

    JTextField targetytext;

    JTextField moneytext;

    JTextField magictext;

    File openfile;

    File savefile;

    private JComboBox eventtypecombo;
}
