package br.com.mcampos.util.system;

import java.util.List;
import org.zkoss.zul.ListModelList;

public class PagingListModel<E> extends ListModelList {

    private Integer _start;

    private Integer _size;

    public PagingListModel(List<E> initialList) {
        super(initialList, true);
    }
}
