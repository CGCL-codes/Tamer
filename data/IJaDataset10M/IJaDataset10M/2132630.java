package it.polimi.tagonto.mapper;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;

public class MapperException extends TagontoException {

    private static final long serialVersionUID = -6869903253094527794L;

    private String message = null;

    public MapperException(Class<? extends IMapper> plugin, String msg) {
        super();
        this.message = String.format(Resources.MSG_MAPPER_EXCEPTION, plugin.getSimpleName()) + msg;
    }

    public MapperException(Class<? extends IMapper> plugin, String msg, Throwable e) {
        super(e);
        this.message = String.format(Resources.MSG_MAPPER_EXCEPTION, plugin.getSimpleName()) + msg;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
