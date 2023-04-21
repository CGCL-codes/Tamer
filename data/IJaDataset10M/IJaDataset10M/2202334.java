package de.lema.transfer.container;

import de.lema.transfer.RequestContainer;
import de.lema.transfer.ServerRequest;

public class DeleteBoRequest<T> extends RequestContainer {

    private static final long serialVersionUID = 1L;

    private final T bo;

    @Override
    public String getParameters() {
        return bo.getClass().toString();
    }

    public DeleteBoRequest(T bo) {
        super(ServerRequest.DeleteBo);
        this.bo = bo;
    }

    public T getBo() {
        return bo;
    }
}
