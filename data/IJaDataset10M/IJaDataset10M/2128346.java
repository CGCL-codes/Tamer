package net.sf.fileexchange.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import javax.swing.JComboBox;
import net.sf.fileexchange.api.ChangeListener;
import net.sf.fileexchange.api.DirectoryLink;
import net.sf.fileexchange.api.FileLink;
import net.sf.fileexchange.api.Model;
import net.sf.fileexchange.api.ResourceTree;
import net.sf.fileexchange.api.ResourceTreeOwner;
import net.sf.fileexchange.api.VirtualFolder;

public class TreeComponentComboBox {

    private enum Item {

        VIRTUAL_FOLDER("Virtual Folder") {

            @Override
            void updateResourceTreeOwnerToThisType(ResourceTreeOwner<?> owner, Model model) {
                owner.setTreeToANewVirtualFolder(model.getFilesUploadedByOthers());
            }
        }
        , FILE_LINK("File Link") {

            @Override
            void updateResourceTreeOwnerToThisType(ResourceTreeOwner<?> owner, Model model) {
                owner.setTreeToANewFileLink(null);
            }
        }
        , DIRECTORY_LINK("Directory Link") {

            @Override
            void updateResourceTreeOwnerToThisType(ResourceTreeOwner<?> owner, Model model) {
                owner.setTreeToANewDirectoryLink(null);
            }
        }
        ;

        private final String text;

        private Item(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return getText();
        }

        public String getText() {
            return text;
        }

        abstract void updateResourceTreeOwnerToThisType(ResourceTreeOwner<?> owner, Model model);

        public static Item typeOf(ResourceTree tree) {
            if (tree instanceof VirtualFolder) {
                return VIRTUAL_FOLDER;
            } else if (tree instanceof FileLink) {
                return FILE_LINK;
            } else if (tree instanceof DirectoryLink) {
                return DIRECTORY_LINK;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static JComboBox createForOwner(final ResourceTreeOwner<?> owner, final Model model) {
        final JComboBox comboBox = new JComboBox(EnumSet.allOf(Item.class).toArray());
        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (model.getResourceTreeLock()) {
                    Item item = (Item) (comboBox.getSelectedItem());
                    ResourceTree tree = owner.getResourceTree();
                    if (Item.typeOf(tree) != item) {
                        item.updateResourceTreeOwnerToThisType(owner, model);
                    }
                }
            }
        });
        comboBox.addHierarchyListener(new AbstractComponentUpdater<JComboBox>(comboBox) {

            private final ChangeListener listener = new ChangeListener() {

                @Override
                public void stateChanged() {
                    updateComponent();
                }
            };

            @Override
            void registerListener() {
                synchronized (model.getResourceTreeLock()) {
                    owner.registerResourceTreeChangeListener(listener);
                }
            }

            @Override
            void unregisterListener() {
                synchronized (model.getResourceTreeLock()) {
                    owner.unregisterResourceTreeChangeListener(listener);
                }
            }

            @Override
            protected void updateComponent() {
                synchronized (model.getResourceTreeLock()) {
                    component.setSelectedItem(Item.typeOf(owner.getResourceTree()));
                }
            }
        });
        return comboBox;
    }
}
