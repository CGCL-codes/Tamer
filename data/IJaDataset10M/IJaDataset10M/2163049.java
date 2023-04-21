package no.ugland.utransprod.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import no.ugland.utransprod.gui.MenuBarBuilderInterface;
import no.ugland.utransprod.gui.OverviewView;
import no.ugland.utransprod.gui.handlers.ArticleTypeViewHandler;
import no.ugland.utransprod.gui.handlers.ArticleTypeViewHandlerFactory;
import no.ugland.utransprod.gui.model.ArticleTypeModel;
import no.ugland.utransprod.model.ArticleType;
import no.ugland.utransprod.service.ArticleTypeManager;
import no.ugland.utransprod.service.AttributeChoiceManager;
import no.ugland.utransprod.service.AttributeManager;
import com.google.inject.Inject;

/**
 * Klasse som h�ndterer opprettelse av vindu for artikkeltype
 * 
 * @author atle.brekka
 */
public class ArticleTypeAction extends AbstractAction {

    /**
	 * 
	 */
    private final MenuBarBuilderInterface menuBarBuilder;

    private static final long serialVersionUID = 1L;

    private ArticleTypeViewHandler articleTypeViewHandler;

    @Inject
    public ArticleTypeAction(MenuBarBuilderInterface aMenuBarBuilder, ArticleTypeViewHandlerFactory aArticleTypeViewHandlerFactory) {
        super("Artikkeltyper...");
        articleTypeViewHandler = aArticleTypeViewHandlerFactory.create(null);
        this.menuBarBuilder = aMenuBarBuilder;
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(final ActionEvent arg0) {
        menuBarBuilder.openFrame(new OverviewView<ArticleType, ArticleTypeModel>(articleTypeViewHandler));
    }
}
