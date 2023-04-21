package uk.co.ordnancesurvey.confluence.testkit.fixtures;

import java.util.Map;
import javax.swing.Icon;
import org.drools.KnowledgeBase;
import org.easymock.EasyMock;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.selection.OWLSelectionModel;
import uk.ac.leeds.comp.ui.action.UIAction;
import uk.ac.leeds.comp.ui.factory.guice.MVCGuiceUtil;
import uk.ac.leeds.comp.ui.itemlist.ItemListView;
import uk.ac.leeds.comp.ui.itemlist.SwingItemListViewWithFocusImpl;
import uk.ac.leeds.comp.ui.selection.impl.SingleSelectionModel;
import uk.co.ordnancesurvey.confluence.IRooEditorKit;
import uk.co.ordnancesurvey.confluence.ProtegeScope;
import uk.co.ordnancesurvey.confluence.RooContext;
import uk.co.ordnancesurvey.confluence.RooContextAware;
import uk.co.ordnancesurvey.confluence.model.RooModelManager;
import uk.co.ordnancesurvey.confluence.model.RooModelManagerImpl;
import uk.co.ordnancesurvey.confluence.model.kanga.KangaOntologyChangeVetoer;
import uk.co.ordnancesurvey.confluence.testkit.fixtures.editorKit.EditorKitWithActiveOntology;
import uk.co.ordnancesurvey.confluence.testkit.fixtures.modelmanager.TestOWLModelManager;
import uk.co.ordnancesurvey.confluence.testkit.fixtures.workspace.MockRooWorkspace;
import uk.co.ordnancesurvey.confluence.ui.IRooWorkspace;
import uk.co.ordnancesurvey.confluence.ui.RooSwingIconProvider;
import uk.co.ordnancesurvey.confluence.ui.action.SuggestNextTaskAction;
import uk.co.ordnancesurvey.confluence.ui.action.nexttask.INextTaskActionFactory;
import uk.co.ordnancesurvey.confluence.ui.action.nexttask.ShowAllSuggestionsAction;
import uk.co.ordnancesurvey.confluence.ui.help.ICflHelpActionLocator;
import uk.co.ordnancesurvey.confluence.ui.itemlist.entity.item.ConfluenceEntityItemModel;
import uk.co.ordnancesurvey.confluence.ui.itemlist.header.CflLabelHeaderView;
import uk.co.ordnancesurvey.confluence.ui.itemlist.nexttask.NextTaskSuggestionListModel;
import uk.co.ordnancesurvey.kanga.guidedog.IDroolsNextTaskList;
import uk.co.ordnancesurvey.kanga.guidedog.condition.IDroolsWorkingMemoryObjectManager;
import uk.co.ordnancesurvey.kanga.guidedog.condition.IOntologyWrapper;
import uk.co.ordnancesurvey.rabbitdao.IRabbitSentenceDao;
import uk.co.ordnancesurvey.rabbitgui.frame.row.RbtSectionRowModel;
import uk.co.ordnancesurvey.rabbitgui.owlapiimpl.OwlApiRabbitPersister;
import uk.co.ordnancesurvey.rabbitgui.relatedsentences.RelatedAssertedRbtDocModelProvider;
import uk.co.ordnancesurvey.rabbitparser.IRabbitToOWLConverterFactory;
import uk.co.ordnancesurvey.rabbitparser.util.RbtHashMap;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public class CflTestModule implements Module {

    public void configure(Binder binder) {
        bindModelManagers(binder);
        binder.bind(RooContextAware.class).to(RooContext.class).asEagerSingleton();
        bindEditorKit(binder);
        binder.bind(IRooWorkspace.class).to(MockRooWorkspace.class).asEagerSingleton();
        bindToMock(binder, RooSwingIconProvider.class);
        bindToMock(binder, ICflHelpActionLocator.class);
        bindToMock(binder, OWLSelectionModel.class);
        final TypeLiteral<Map<String, Icon>> map = new TypeLiteral<Map<String, Icon>>() {
        };
        binder.bind(map).toInstance(new RbtHashMap<String, Icon>());
        binder.bind(MVCGuiceUtil.getUIViewTypeLiteral(String.class)).annotatedWith(Names.named("SectionHeader")).to(CflLabelHeaderView.class);
        bindMvcComponents(binder);
        bindToMock(binder, KangaOntologyChangeVetoer.class);
        bindRbtToOwlConverterFactory(binder);
        bindRelatedSentencesProviders(binder);
        bindRbtFrame(binder);
        bindToMock(binder, MVCGuiceUtil.getSelectionModelTypedLiteral(ConfluenceEntityItemModel.class));
        bindNextTaskSubModule(binder);
    }

    protected void bindModelManagers(Binder binder) {
        binder.bind(OWLModelManager.class).annotatedWith(Names.named("OWLEditorKits")).to(TestOWLModelManager.class).asEagerSingleton();
        binder.bind(OWLModelManager.class).annotatedWith(ProtegeScope.class).to(RooModelManager.class);
        binder.bind(OWLModelManager.class).to(RooModelManager.class);
        binder.bind(RooModelManager.class).to(RooModelManagerImpl.class).asEagerSingleton();
    }

    protected void bindRbtFrame(Binder binder) {
        binder.bind(MVCGuiceUtil.getSelectionModelTypedLiteral(RbtSectionRowModel.class)).toInstance(new SingleSelectionModel<RbtSectionRowModel>());
    }

    protected void bindRbtToOwlConverterFactory(Binder binder) {
        bindToMock(binder, IRabbitToOWLConverterFactory.class);
    }

    protected void bindMvcComponents(Binder binder) {
        binder.bind(ItemListView.class).to(SwingItemListViewWithFocusImpl.class);
    }

    protected void bindRelatedSentencesProviders(Binder binder) {
        bindToMock(binder, OwlApiRabbitPersister.class);
        bindToMock(binder, RelatedAssertedRbtDocModelProvider.class);
        bindToMock(binder, IRabbitSentenceDao.class);
    }

    protected void bindNextTaskSubModule(Binder binder) {
        bindToMock(binder, KnowledgeBase.class);
        bindToMock(binder, IDroolsNextTaskList.class);
        bindToMock(binder, IDroolsWorkingMemoryObjectManager.class);
        bindToMock(binder, IOntologyWrapper.class);
        binder.bind(UIAction.class).annotatedWith(Names.named("NextTaskSuggestion")).to(SuggestNextTaskAction.class);
        binder.bind(UIAction.class).annotatedWith(Names.named("ShowAllSuggestions")).to(ShowAllSuggestionsAction.class);
        bindToMock(binder, INextTaskActionFactory.class);
        bindToMock(binder, NextTaskSuggestionListModel.class);
    }

    protected void bindEditorKit(Binder binder) {
        binder.bind(IRooEditorKit.class).to(EditorKitWithActiveOntology.class).asEagerSingleton();
    }

    protected final <T> void bindToMock(Binder binder, Class<T> aInterfaceClass) {
        binder.bind(aInterfaceClass).toInstance(EasyMock.createMock(aInterfaceClass));
    }

    private <T> void bindToMock(Binder binder, TypeLiteral<T> aTypeLiteral) {
        @SuppressWarnings("unchecked") T mockInstance = (T) EasyMock.createMock(aTypeLiteral.getRawType());
        binder.bind(aTypeLiteral).toInstance(mockInstance);
    }
}
