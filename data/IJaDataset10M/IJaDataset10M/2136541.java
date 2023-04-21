package pl.n3fr0.n3talk.mirror.entity;

public interface MirrorMapModificator {

    String getObjectUID();

    String getName();

    Kind getKind();

    Object getKey();

    Object getValue();

    enum Kind {

        PUT, REMOVE
    }
}
