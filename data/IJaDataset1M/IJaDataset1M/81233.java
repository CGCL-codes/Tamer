package org.aplikator.client.rpc.impl;

import org.aplikator.client.data.PrimaryKey;
import org.aplikator.client.descriptor.PropertyDTO;
import org.aplikator.client.descriptor.ViewDTO;
import org.aplikator.client.rpc.Command;

@SuppressWarnings("serial")
public class GetPage implements Command<GetPageResponse> {

    private ViewDTO view;

    private PropertyDTO<Integer> ownerProperty;

    private PrimaryKey ownerPrimaryKey;

    private int pageOffset;

    private int pageSize;

    @SuppressWarnings("unused")
    private GetPage() {
    }

    public GetPage(ViewDTO view, PropertyDTO<Integer> ownerProperty, PrimaryKey ownerPrimaryKey, int pageOffset, int pageSize) {
        super();
        this.view = view;
        this.ownerProperty = ownerProperty;
        this.ownerPrimaryKey = ownerPrimaryKey;
        this.pageOffset = pageOffset;
        this.pageSize = pageSize;
    }

    public ViewDTO getView() {
        return view;
    }

    public PropertyDTO<Integer> getOwnerProperty() {
        return ownerProperty;
    }

    public PrimaryKey getOwnerPrimaryKey() {
        return ownerPrimaryKey;
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public int getPageSize() {
        return pageSize;
    }
}
