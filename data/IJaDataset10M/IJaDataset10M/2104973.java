package cn.imgdpu.compo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import cn.imgdpu.util.XmlProcess;
import com.swtdesigner.SWTResourceManager;

public class ViewTableInfoCompo extends Composite {

    private static StyledText friDT;

    private static StyledText thuDT;

    private static StyledText wenDT;

    private static StyledText tueDT;

    private static StyledText monDT;

    private static CLabel classLabel;

    /**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
    public ViewTableInfoCompo(Composite parent, int style) {
        super(parent, style);
        setLayout(new FormLayout());
        classLabel = new CLabel(this, SWT.NONE);
        classLabel.setImage(SWTResourceManager.getImage(ViewTableInfoCompo.class, "/cn/imgdpu/ico/menu_bar.gif"));
        final FormData fd_classLabel = new FormData();
        fd_classLabel.bottom = new FormAttachment(0, 30);
        fd_classLabel.right = new FormAttachment(100, -245);
        fd_classLabel.top = new FormAttachment(0, 5);
        fd_classLabel.left = new FormAttachment(0, 5);
        classLabel.setLayoutData(fd_classLabel);
        classLabel.setText("当前班级：");
        Composite dayCompo;
        dayCompo = new Composite(this, SWT.NONE);
        dayCompo.setLayout(new FillLayout());
        final FormData fd_dayCompo = new FormData();
        fd_dayCompo.bottom = new FormAttachment(100, -5);
        fd_dayCompo.left = new FormAttachment(0, 5);
        fd_dayCompo.right = new FormAttachment(100, -5);
        dayCompo.setLayoutData(fd_dayCompo);
        final Group monDayGro = new Group(dayCompo, SWT.NONE);
        monDayGro.setText("星期一");
        monDayGro.setLayout(new FillLayout());
        monDT = new StyledText(monDayGro, SWT.WRAP | SWT.READ_ONLY | SWT.BORDER);
        final Group tueDayGro = new Group(dayCompo, SWT.NONE);
        tueDayGro.setText("星期二");
        tueDayGro.setLayout(new FillLayout());
        tueDT = new StyledText(tueDayGro, SWT.WRAP | SWT.READ_ONLY | SWT.BORDER);
        final Group wenDayGro = new Group(dayCompo, SWT.NONE);
        wenDayGro.setText("星期三");
        wenDayGro.setLayout(new FillLayout());
        wenDT = new StyledText(wenDayGro, SWT.WRAP | SWT.READ_ONLY | SWT.BORDER);
        final Group thuDayGro = new Group(dayCompo, SWT.NONE);
        thuDayGro.setText("星期四");
        thuDayGro.setLayout(new FillLayout());
        thuDT = new StyledText(thuDayGro, SWT.WRAP | SWT.READ_ONLY | SWT.BORDER);
        final Group firDayGro = new Group(dayCompo, SWT.NONE);
        firDayGro.setText("星期五");
        firDayGro.setLayout(new FillLayout());
        friDT = new StyledText(firDayGro, SWT.WRAP | SWT.READ_ONLY | SWT.BORDER);
        Button setTableBut;
        setTableBut = new Button(this, SWT.NONE);
        fd_dayCompo.top = new FormAttachment(setTableBut, 5, SWT.BOTTOM);
        setTableBut.setImage(SWTResourceManager.getImage(ViewTableInfoCompo.class, "/cn/imgdpu/ico/add_action.gif"));
        final FormData fd_setTableBut = new FormData();
        fd_setTableBut.left = new FormAttachment(100, -125);
        fd_setTableBut.bottom = new FormAttachment(0, 30);
        fd_setTableBut.top = new FormAttachment(0, 5);
        fd_setTableBut.right = new FormAttachment(100, -5);
        setTableBut.setLayoutData(fd_setTableBut);
        setTableBut.setText("设置为课表");
        setTableBut.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event e) {
                cn.imgdpu.util.XmlProcess.setCdata("classname", classLabel.getText());
                cn.imgdpu.util.XmlProcess.setCdata("monday", monDT.getText());
                cn.imgdpu.util.XmlProcess.setCdata("tueday", tueDT.getText());
                cn.imgdpu.util.XmlProcess.setCdata("wenday", wenDT.getText());
                cn.imgdpu.util.XmlProcess.setCdata("thuday", thuDT.getText());
                cn.imgdpu.util.XmlProcess.setCdata("friday", friDT.getText());
                cn.imgdpu.util.XmlProcess.setAct("tableact", "1");
                MessageBox box = new MessageBox(cn.imgdpu.GSAGUI.shell, SWT.ICON_WORKING | SWT.OK);
                box.setText("设置");
                box.setMessage("设置成功!");
                box.open();
                cn.imgdpu.dialog.ViewTableInfoDialog.closeShell();
            }
        });
    }

    @Override
    protected void checkSubclass() {
    }

    public void setText(int itemNo) {
        ArrayList<String> data = new XmlProcess().getTableXML(itemNo);
        if (data.size() == 6) {
            classLabel.setText(data.get(0));
            monDT.setText(doSplit(data.get(1)));
            tueDT.setText(doSplit(data.get(2)));
            wenDT.setText(doSplit(data.get(3)));
            thuDT.setText(doSplit(data.get(4)));
            friDT.setText(doSplit(data.get(5)));
        } else {
            cn.imgdpu.GSAGUI.setStatusAsyn("课程表数据有误！");
        }
    }

    public String doSplit(String classTableI) {
        ArrayList<String> timeTableSplit = new ArrayList<String>();
        String s = new String();
        String tempStr = classTableI;
        Pattern pattern = Pattern.compile("\\{(\\d),(\\d)\\}([^{ABC]+)([ABC][0-9]\\-[0-9]{3})?");
        Matcher matcher = pattern.matcher(tempStr);
        while (matcher.find()) {
            for (int j = 1; j <= matcher.groupCount(); j++) {
                timeTableSplit.add(matcher.group(j));
            }
        }
        for (int i = 0; i < timeTableSplit.size(); i += 4) {
            s += ">> " + timeTableSplit.get(i) + "-" + timeTableSplit.get(i + 1) + "节" + "  " + timeTableSplit.get(i + 3) + "\n";
            s += "  " + timeTableSplit.get(i + 2) + "\n\n";
        }
        return s;
    }
}
