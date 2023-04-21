package javabook.ch07;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.List;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Jv_7_4 extends Frame implements ActionListener, ItemListener, AdjustmentListener {

    Checkbox ch, da, so, se, je;

    Checkbox gch, gda, gso, gse, gje;

    CheckboxGroup cbg;

    Label label1, label2, label3, label4, label5, lb0;

    Scrollbar vSlider, hSlider;

    Canvas imgCanvas;

    Button bt0;

    String msg1 = " ";

    String msg2 = " ";

    public Jv_7_4() {
        super("단순 컴포넌트 - 체크박스와 라디오 버튼/ 리스트와 초이스 / 수직 및 수평 스크롤바 / 이미지 캔버스 ");
        setLayout(new FlowLayout());
        ch = new Checkbox("천안", null, true);
        da = new Checkbox("당진");
        so = new Checkbox("속초");
        se = new Checkbox("서울");
        je = new Checkbox("제주");
        add(ch);
        add(da);
        add(so);
        add(se);
        add(je);
        cbg = new CheckboxGroup();
        gch = new Checkbox("천안", cbg, false);
        gda = new Checkbox("당진", cbg, false);
        gso = new Checkbox("속초", cbg, true);
        gse = new Checkbox("서울", cbg, false);
        gje = new Checkbox("제주", cbg, false);
        add(gch);
        add(gda);
        add(gso);
        add(gse);
        add(gje);
        List list = new List(3, false);
        list.add("천안");
        list.add("당진");
        list.add("속초");
        list.add("서울");
        list.add("제주");
        add(list);
        Choice choice = new Choice();
        choice.add("천안");
        choice.add("당진");
        choice.add("속초");
        choice.add("서울");
        choice.add("제주");
        add(choice);
        label4 = new Label("<수직 또는 수평스크롤바 움직이면 ==> 캔버스 색깔 바뀜 > ", Label.CENTER);
        add(label4);
        label1 = new Label("수직 스크롤바 : ", Label.CENTER);
        label2 = new Label("수평 스크롤바 : ", Label.CENTER);
        vSlider = new Scrollbar(Scrollbar.VERTICAL, 0, 1, 0, 100);
        hSlider = new Scrollbar(Scrollbar.HORIZONTAL, 0, 4, 0, 50);
        add(label1);
        add(vSlider);
        add(label2);
        add(hSlider);
        label3 = new Label("이미지 캔버스 : ", Label.CENTER);
        imgCanvas = new Canvas();
        imgCanvas.setBackground(Color.yellow);
        imgCanvas.setSize(200, 150);
        add(label3);
        add(imgCanvas);
        lb0 = new Label("종료 버튼 : ");
        bt0 = new Button("종료");
        add(lb0);
        add(bt0);
        setSize(730, 450);
        setVisible(true);
        bt0.addActionListener(this);
        ch.addItemListener(this);
        da.addItemListener(this);
        so.addItemListener(this);
        se.addItemListener(this);
        je.addItemListener(this);
        gch.addItemListener(this);
        gda.addItemListener(this);
        gso.addItemListener(this);
        gse.addItemListener(this);
        gje.addItemListener(this);
        vSlider.addAdjustmentListener(this);
        hSlider.addAdjustmentListener(this);
        list.addItemListener(this);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Jv_7_4 c1 = new Jv_7_4();
    }

    public void actionPerformed(ActionEvent evt) {
        String str = evt.getActionCommand();
        if (str.equals("종료")) System.exit(0);
    }

    public void itemStateChanged(ItemEvent ie) {
        repaint();
    }

    public void adjustmentValueChanged(AdjustmentEvent ae) {
        imgCanvas.setBackground(Color.green);
        imgCanvas.repaint();
    }

    public void paint(Graphics g) {
        msg1 = "<체크상자를 선택하면 상태를 알수 있음 > ";
        g.drawString(msg1, 100, 320);
        msg1 = "<천안(체크상자): " + ch.getState();
        g.drawString(msg1, 100, 340);
        msg1 = "<당진(체크상자): " + da.getState();
        g.drawString(msg1, 100, 360);
        msg1 = "<속초(체크상자): " + so.getState();
        g.drawString(msg1, 100, 380);
        msg1 = "<서울(체크상자): " + se.getState();
        g.drawString(msg1, 100, 400);
        msg1 = "<제주(체크상자): " + je.getState();
        g.drawString(msg1, 100, 420);
        msg2 = "<라디오 버튼을  선택하면 상태를 알수 있음 > ";
        g.drawString(msg2, 400, 320);
        msg2 = "<선택한 지명(라디오 버튼) : ";
        msg2 += cbg.getSelectedCheckbox().getLabel();
        g.drawString(msg2, 400, 340);
    }
}
