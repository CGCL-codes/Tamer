package com.nepxion.demo.component.searcher.ip.tencent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import com.nepxion.swing.border.BorderManager;
import com.nepxion.swing.searcher.ip.tencent.JTencentIPPanel;

public class DemoTencentIPPanel extends JPanel {

    public DemoTencentIPPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(new TencentIPPanel());
    }

    public class TencentIPPanel extends JPanel {

        public TencentIPPanel() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBorder(BorderManager.createComplexTitledBorder("���IP��ַ��ѯ����λ��"));
            JTencentIPPanel ipPanel = new JTencentIPPanel();
            add(ipPanel);
        }
    }
}
