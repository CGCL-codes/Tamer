package chrriis.udoc.ui.widgets;

import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import chrriis.udoc.model.ClassInfo;
import chrriis.udoc.model.Util;
import chrriis.udoc.ui.ClassComponent;

public class ClassDeclarationComponent extends JPanel {

    public ClassDeclarationComponent(ClassComponent classComponent, String classDeclaration, int[] classNamesIndices, ClassInfo[] classInfos) {
        super(new FlowLayout(FlowLayout.LEADING, 0, 0));
        setOpaque(false);
        if (classNamesIndices.length > 0) {
            add(new JLabel(classDeclaration.substring(0, classNamesIndices[0])));
        }
        int lastIndex = 0;
        for (int i = 0; i < classInfos.length; i++) {
            int start = classNamesIndices[i * 2];
            int end = classNamesIndices[i * 2 + 1];
            if (lastIndex != start) {
                add(new JLabel(classDeclaration.substring(lastIndex, start)));
            }
            lastIndex = end;
            add(new ClassLink(classComponent, Util.unescapeClassName(classDeclaration.substring(start, end)), classInfos[i]));
        }
        if (lastIndex < classDeclaration.length()) {
            add(new JLabel(classDeclaration.substring(lastIndex, classDeclaration.length())));
        }
    }

    public void setFont(Font font) {
        super.setFont(font);
        for (int i = getComponentCount() - 1; i >= 0; i--) {
            getComponent(i).setFont(font);
        }
    }
}
