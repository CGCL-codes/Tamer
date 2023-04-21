package de.andreavicentini.magicphoto.domain.layer.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.magiclabs.basix.IClosure;
import org.magiclabs.basix.Magics;
import de.andreavicentini.magicphoto.domain.directory.IDirectory;
import de.andreavicentini.magicphoto.domain.layer.ILayer;
import de.andreavicentini.magicphoto.domain.pictures.IPicture;

public class CachingLayer extends DelegatingLayer {

    public CachingLayer(ILayer delegate) {
        super(delegate);
    }

    private final Map<IDirectory, IPicture[]> cachePictures = new HashMap<IDirectory, IPicture[]>();

    private final Map<IPicture, IDirectory> cacheParents = new HashMap<IPicture, IDirectory>();

    @Override
    public IPicture[] listPictures(final IDirectory node) {
        load(node);
        IPicture[] result = this.cachePictures.get(node);
        Magics.each(result, new IClosure<IPicture>() {

            public void process(IPicture object) {
                cacheParents.put(object, node);
            }
        });
        return result;
    }

    private void load(IDirectory node) {
        if (!this.cachePictures.containsKey(node)) this.cachePictures.put(node, this.delegate.listPictures(node));
    }

    @Override
    public IDirectory getDirectory(IPicture picture) {
        return this.cacheParents.get(picture);
    }

    public void remove(IPicture picture) {
        IDirectory directory = this.getDirectory(picture);
        load(directory);
        this.cachePictures.put(directory, removeFromList(this.cachePictures.get(directory), picture));
    }

    private IPicture[] removeFromList(IPicture[] pictures, IPicture picture) {
        List list = new ArrayList(Arrays.asList(pictures));
        list.remove(picture);
        return (IPicture[]) list.toArray(new IPicture[0]);
    }
}
