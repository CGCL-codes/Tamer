package org.dengues.ui.editors;

import java.util.ArrayList;
import java.util.List;
import org.dengues.core.DenguesCorePlugin;
import org.dengues.core.ExceptionOperation;
import org.dengues.core.prefs.IDenguesPrefsConstant;
import org.dengues.core.warehouse.ENodeCategoryName;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.ui.palette.PaletteEditPartFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.PaletteViewerKeyHandler;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Utility class for GEF components editor.
 * 
 * 
 * /** Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2008-1-7 qiang.zhang $
 * 
 */
public class GEFEditorUtils {

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "createPalettePreferences".
     * 
     * @return
     */
    public static FlyoutPreferences createPalettePreferences() {
        return createPalettePreferences(DenguesCorePlugin.getDefault().getPreferenceStore());
    }

    /**
     * Return a FlyoutPreferences instance used to save/load the preferences of a flyout palette.
     */
    public static FlyoutPreferences createPalettePreferences(final IPreferenceStore store) {
        return new FlyoutPreferences() {

            /**
             * Qiang.Zhang.Adolf@gmail.com Comment method "getPreferenceStore".
             * 
             * @return
             */
            private IPreferenceStore getPreferenceStore() {
                return store;
            }

            public int getDockLocation() {
                return getPreferenceStore().getInt(IDenguesPrefsConstant.PALETTE_DOCK_LOCATION);
            }

            public int getPaletteState() {
                return getPreferenceStore().getInt(IDenguesPrefsConstant.PALETTE_STATE);
            }

            public int getPaletteWidth() {
                return getPreferenceStore().getInt(IDenguesPrefsConstant.PALETTE_SIZE);
            }

            public void setDockLocation(int location) {
                getPreferenceStore().setValue(IDenguesPrefsConstant.PALETTE_DOCK_LOCATION, location);
            }

            public void setPaletteState(int state) {
                getPreferenceStore().setValue(IDenguesPrefsConstant.PALETTE_STATE, state);
            }

            public void setPaletteWidth(int width) {
                getPreferenceStore().setValue(IDenguesPrefsConstant.PALETTE_SIZE, width);
            }
        };
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "createPaletteViewerProvider".
     * 
     * @param defaultEditDomain
     * @return
     */
    public static PaletteViewerProvider createPaletteViewerProvider(DefaultEditDomain defaultEditDomain) {
        return new GenericPaletteViewerProvider(defaultEditDomain);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com GEFEditorUtils class global comment. Detailled comment <br/>
     * 
     * $Id: Dengues.epf Qiang.Zhang.Adolf@gmail.com 2008-1-23 qiang.zhang $
     * 
     */
    static class GenericPaletteViewerProvider extends PaletteViewerProvider {

        EditDomain graphicalViewerDomain;

        /**
         * Qiang.Zhang.Adolf@gmail.com GenericPaletteViewerProvider constructor comment.
         * 
         * @param graphicalViewerDomain
         */
        public GenericPaletteViewerProvider(EditDomain graphicalViewerDomain) {
            super(graphicalViewerDomain);
            this.graphicalViewerDomain = graphicalViewerDomain;
        }

        @Override
        public PaletteViewer createPaletteViewer(Composite parent) {
            PaletteViewer pViewer = new GenericPaletteViewer(graphicalViewerDomain);
            pViewer.createControl(parent);
            configurePaletteViewer(pViewer);
            hookPaletteViewer(pViewer);
            pViewer.getPaletteViewerPreferences().setLayoutSetting(PaletteViewerPreferences.LAYOUT_COLUMNS);
            pViewer.getPaletteViewerPreferences().setUseLargeIcons(PaletteViewerPreferences.LAYOUT_COLUMNS, true);
            return pViewer;
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com GEFEditorUtils class global comment. Detailled comment <br/>
     * 
     * $Id: Dengues.epf Qiang.Zhang.Adolf@gmail.com 2008-1-23 qiang.zhang $
     * 
     */
    static class GenericPaletteViewer extends PaletteViewer {

        /**
         * Qiang.Zhang.Adolf@gmail.com GenericPaletteViewer constructor comment.
         * 
         * @param graphicalViewerDomain
         */
        public GenericPaletteViewer(EditDomain graphicalViewerDomain) {
            setEditDomain(graphicalViewerDomain);
            setKeyHandler(new PaletteViewerKeyHandler(this));
            setEditPartFactory(new GenericPaletteEditPartFactory());
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com GEFEditorUtils class global comment. Detailled comment <br/>
     * 
     * $Id: Dengues.epf Qiang.Zhang.Adolf@gmail.com 2008-1-23 qiang.zhang $
     * 
     */
    static class GenericPaletteEditPartFactory extends PaletteEditPartFactory {

        @Override
        protected EditPart createDrawerEditPart(EditPart parentEditPart, Object model) {
            return new GenericDrawerEditPart((PaletteDrawer) model);
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com GEFEditorUtils class global comment. Detailled comment <br/>
     * 
     * $Id: Dengues.epf Qiang.Zhang.Adolf@gmail.com 2008-1-23 qiang.zhang $
     * 
     */
    static class GenericDrawerEditPart extends DrawerEditPart {

        private int childLevel = 0;

        /**
         * Qiang.Zhang.Adolf@gmail.com GenericDrawerEditPart constructor comment.
         * 
         * @param drawer
         */
        public GenericDrawerEditPart(PaletteDrawer drawer) {
            super(drawer);
        }

        @Override
        public IFigure createFigure() {
            if (getParent() instanceof GenericDrawerEditPart) {
                GenericDrawerEditPart parent = (GenericDrawerEditPart) getParent();
                childLevel = parent.childLevel + 1;
                GenericDrawerFigure fig = new GenericDrawerFigure(getViewer().getControl(), childLevel) {

                    IFigure buildTooltip() {
                        return createToolTip();
                    }
                };
                fig.setExpanded(getDrawer().isInitiallyOpen());
                fig.setPinned(getDrawer().isInitiallyPinned());
                fig.getCollapseToggle().addFocusListener(new FocusListener.Stub() {

                    @Override
                    public void focusGained(FocusEvent fe) {
                        getViewer().select(GenericDrawerEditPart.this);
                    }
                });
                return fig;
            }
            return super.createFigure();
        }

        /**
         * Qiang.Zhang.Adolf@gmail.com GEFEditorUtils.GenericDrawerEditPart class global comment. Detailled comment
         * <br/>
         * 
         * $Id: Dengues.epf Qiang.Zhang.Adolf@gmail.com 2008-1-23 qiang.zhang $
         * 
         */
        class GenericDrawerFigure extends DrawerFigure {

            private static final int COLOR_INCREMENT = 15;

            private static final int X_OFFSET = 17;

            /**
             * Qiang.Zhang.Adolf@gmail.com GenericDrawerFigure constructor comment.
             * 
             * @param control
             * @param childLevel
             */
            public GenericDrawerFigure(Control control, int childLevel) {
                super(control);
                Color baseColor = control.getBackground();
                Color backgroundColor = new Color(Display.getCurrent(), getNewValue(baseColor.getRed(), childLevel), getNewValue(baseColor.getGreen(), childLevel), getNewValue(baseColor.getBlue(), childLevel));
                getContentPane().setBackgroundColor(backgroundColor);
            }

            /**
             * Qiang.Zhang.Adolf@gmail.com Comment method "getNewValue".
             * 
             * @param oldValue
             * @param childLevel
             * @return
             */
            private int getNewValue(int oldValue, int childLevel) {
                int result = oldValue - childLevel * COLOR_INCREMENT;
                return (result > 0 ? result : 0);
            }

            @Override
            public Rectangle getBounds() {
                return new Rectangle(bounds.x + X_OFFSET, bounds.y, bounds.width, bounds.height);
            }
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getOpenedEditors".
     * 
     * @return
     */
    private static List<String> getOpenedEditors() {
        List<String> list = new ArrayList<String>();
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow != null) {
            IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
            if (activePage != null) {
                IEditorReference[] editorReferences = activePage.getEditorReferences();
                if (editorReferences != null) {
                    for (IEditorReference editorReference : editorReferences) {
                        try {
                            IEditorInput editorInput = editorReference.getEditorInput();
                            if (editorInput instanceof AbstractEditorInput) {
                                list.add(((AbstractEditorInput) editorInput).getEditorUniqueName());
                            }
                        } catch (PartInitException ex) {
                            ExceptionOperation.operate(ex);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "isNodeActived".
     * 
     * @param objName
     * @param name
     * @return
     */
    public static boolean isNodeActived(String objName, ENodeCategoryName name) {
        return getOpenedEditors().contains(getEditorUniqueName(objName, name));
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getEditorUniqueName".
     * 
     * @param objName
     * @param name
     * @return
     */
    public static String getEditorUniqueName(String objName, ENodeCategoryName name) {
        return name.getName() + name.getVersion() + objName;
    }
}
