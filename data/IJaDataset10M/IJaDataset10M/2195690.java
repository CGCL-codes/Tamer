package net.entelijan.cobean.examples.showcase.config.text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import net.entelijan.cobean.data.literature.ILitService;
import net.entelijan.cobean.data.literature.LitBook;
import net.entelijan.config.CobeanConfigFactory;
import net.entelijan.config.ICobeanConfig;
import net.entelijan.scf.IShowcase;
import org.springframework.beans.BeanUtils;

public class ConfigMultiFailureTextShowcase implements IShowcase<ConfigMultiFailureTextPanel> {

    private LitBook book;

    private ILitService service = null;

    public ConfigMultiFailureTextShowcase() {
        super();
    }

    @Override
    public void init(final ConfigMultiFailureTextPanel view) {
        book = service.loadAllBooks().get(4);
        ICobeanConfig cfg = CobeanConfigFactory.createConfig();
        book = cfg.bind(book, view, null);
        view.getShowModelButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(view, "" + book);
            }
        });
        view.getDefaultValuesButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                BeanUtils.copyProperties(service.loadAllBooks().get(4), book);
            }
        });
    }

    public LitBook getBook() {
        return book;
    }

    public void setBook(LitBook book) {
        this.book = book;
    }

    public ILitService getService() {
        return service;
    }

    public void setService(ILitService service) {
        this.service = service;
    }
}
