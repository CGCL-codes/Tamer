package com.dukesoftware.viewlon3.gui.infopanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.text.NumberFormat;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import com.dukesoftware.utils.awt.AwtUtils;
import com.dukesoftware.utils.swing.others.SwingUtils;
import com.dukesoftware.viewlon3.data.common.Const;
import com.dukesoftware.viewlon3.data.common.DataController;
import com.dukesoftware.viewlon3.data.common.DataManagerCore;
import com.dukesoftware.viewlon3.data.internal.ClassObjectArray;
import com.dukesoftware.viewlon3.utils.viewlon.ComponentSupport;
import com.dukesoftware.viewlon3.utils.viewlon.Fonts;

/**
 * Viewlonの{@link InfoPanel}の部分パネルで用いる抽象JPanelクラスです。
 * 
 * 
 *
 *
 */
public abstract class TextLabelPanel extends JPanel implements Const {

    private static final int GAP = 5;

    protected final int panelx;

    protected final int panely;

    protected final DataController<DataManagerCore> d_con;

    protected final NumberFormat formatter = NumberFormat.getNumberInstance();

    public TextLabelPanel(String title, int panelx, int panely, DataController<DataManagerCore> d_con) {
        this.d_con = d_con;
        this.panelx = panelx;
        this.panely = panely;
        setBorder(new TitledBorder(new LineBorder(Color.GRAY, 2), title, TitledBorder.LEFT, TitledBorder.TOP, Fonts.FONT_TITLE));
        ComponentSupport.setFormatDigits(1, formatter);
        createConponents();
    }

    /**
	 * コンポーネントを作成するための抽象メソッドです。
	 *
	 */
    protected abstract void createConponents();

    /**
	 * 情報フィールドに情報を書き込みます。サブクラスに実装をします。
	 * @param flag
	 */
    protected abstract void writeInfoField(boolean flag);

    /**
	 * {@link GridLayout}の{@link GridLayout#setVgap(int)}を5に設定した{@link JPanel}を生成する静的メソッドです。
	 * @return 生成された{@link JPanel}
	 */
    protected static JPanel createPanel() {
        return new JPanel(new GridLayout(0, 1, 0, GAP));
    }

    /**
	 * 情報パネル用の汎用GUI作成メソッドです。
	 * 
	 * @param text
	 * @param body
	 * @param x
	 * @param y
	 * @return
	 */
    protected static final JPanel createInfoPanel(String text, JLabel body, int x, int y) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        AwtUtils.setComponentSizeFixed(panel, x, y);
        panel.add(SwingUtils.createPanelwithLabel(text, null, null, x / 3, y));
        panel.add(body);
        return panel;
    }
}
