package br.com.renatoccosta.regexrenamer.view;

import br.com.renatoccosta.regexrenamer.RenamedFile;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author renato
 */
public abstract class RenamerListModel extends AbstractListModel {

    protected List<RenamedFile> files;

    public RenamerListModel(List<RenamedFile> files) {
        this.files = files;
    }

    public int getSize() {
        return files.size();
    }

    public void refresh() {
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
