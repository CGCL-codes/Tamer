package lablog.gui.comp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import lablog.gui.selectors.AbstractSelector;
import lablog.gui.selectors.SelectorFactory;
import lablog.gui.selectors.SubjectSelector;
import lablog.gui.views.AbstractView;
import lablog.gui.views.ViewFactory;
import lablog.util.orm.auto.Subject;

public class SubjectPanel extends MainContentPanel {

    private AbstractSelector<Subject> selector;

    private AbstractView<Subject> view;

    private JScrollPane scrollPane;

    public SubjectPanel() {
        super("Subjects", SHOW_PROJECT_SELECTOR);
        getContentContainer().setLayout(new BorderLayout());
        selector = SelectorFactory.createSelector(Subject.class);
        getContentContainer().add(selector, BorderLayout.WEST);
        view = ViewFactory.createView(Subject.class);
        scrollPane = new JScrollPane(view, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        getContentContainer().add(scrollPane, BorderLayout.CENTER);
        selector.addSelectListener(new SelectListener());
    }

    public void refresh() {
        selector.refresh();
    }

    private final class SelectListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("SubjectPanel#SelectListener.actionPerformed()");
            if (e.getSource() instanceof SubjectSelector) {
                Subject sc = ((SubjectSelector) e.getSource()).getSelectedElement();
                if (sc != null) view.setElement(sc);
            }
        }
    }
}
