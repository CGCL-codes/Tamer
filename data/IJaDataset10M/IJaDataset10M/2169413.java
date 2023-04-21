package de.schwarzrot.ui.control;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.springframework.context.MessageSource;
import ca.odell.glazedlists.EventList;
import de.schwarzrot.app.config.DesktopConfig;
import de.schwarzrot.app.domain.DriveMapping;
import de.schwarzrot.app.support.ApplicationServiceProvider;
import de.schwarzrot.ui.control.support.AbstractDialogBase;
import de.schwarzrot.ui.image.ImageFactory;

/**
 * a dialog implementation to manage {@code DriveMappings}. Uses
 * {@code MappingsTableView} as client area.
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 */
public class MappingsDialog extends AbstractDialogBase {

    private static final long serialVersionUID = 713L;

    private static ImageFactory imgFactory;

    private static MessageSource msgSource;

    public MappingsDialog(DesktopConfig config) {
        this.config = config;
        setStartupSize(new Dimension(600, 500));
    }

    @Override
    protected JComponent buildPanel() {
        if (imgFactory == null) imgFactory = ApplicationServiceProvider.getService(ImageFactory.class);
        JPanel pane = new JPanel();
        JToolBar tb = new JToolBar();
        Icon icon = imgFactory.createIcon(getClass().getSimpleName() + ".add.icon");
        JButton bt = new JButton(icon);
        bt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                doAddMapping(ae);
            }
        });
        tb.add(bt);
        icon = imgFactory.createIcon(getClass().getSimpleName() + ".remove.icon");
        bt = new JButton(icon);
        bt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                doRemoveMapping(ae);
            }
        });
        tb.add(bt);
        pane.setLayout(new BorderLayout());
        view = new MappingsTableView(config.getDriveMappings().values());
        view.setSmallToolBar(tb);
        pane.add(view.buildPanel(), BorderLayout.CENTER);
        return new ApplicationPage(getClass().getSimpleName(), pane);
    }

    protected void doAddMapping(ActionEvent ae) {
        DriveMapping m = new DriveMapping();
        MappingDetailDialog dlg = new MappingDetailDialog(m, view.getList());
        if (dlg.showDialog(getDialog()) == MappingDetailDialog.APPROVE_OPTION) {
            m.setParent(config);
            view.add(m);
        }
    }

    protected void doRemoveMapping(ActionEvent ae) {
        EventList<DriveMapping> list = view.getSelectionModel().getSelected();
        if (list != null && list.size() > 0) {
            for (DriveMapping cur : list) view.remove(cur);
        }
    }

    @Override
    protected String getDialogTitle() {
        if (msgSource == null) msgSource = ApplicationServiceProvider.getService(MessageSource.class);
        return msgSource.getMessage(getClass().getSimpleName() + ".title", null, getClass().getSimpleName() + ".title", null);
    }

    @Override
    protected void performApprove() {
        config.setMappings(view.getList());
    }

    private MappingsTableView view;

    private DesktopConfig config;
}
