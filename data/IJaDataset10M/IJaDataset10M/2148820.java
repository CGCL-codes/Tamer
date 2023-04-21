package net.sourceforge.arcamplayer.library.xmlhandlers;

import java.util.ListIterator;
import javax.xml.bind.JAXBElement;
import net.sourceforge.arcamplayer.library.collection.SongRef;
import net.sourceforge.arcamplayer.library.collection.StaticList;
import net.sourceforge.arcamplayer.library.xmltypes.SongType;
import net.sourceforge.arcamplayer.library.xmltypes.StaticListType;

/**
 * <p>Conversor para las listas estáticas.</p>
 * @author David Arranz Oveja
 * @author Pelayo Campa González-Nuevo
 */
public class StaticListConversionHandler extends AbstractConversionHandler {

    /**
	 * 
	 */
    @Override
    public StaticList convertFromPersistenceModelToClientModel(Object obj) {
        StaticListType sltype = (StaticListType) obj;
        StaticList staticlist = new StaticList(sltype.getName());
        ListIterator<JAXBElement<Object>> li = sltype.listIterator();
        if (li != null) {
            while (li.hasNext()) {
                SongType st = (SongType) ((JAXBElement<Object>) li.next()).getValue();
                AbstractConversionHandler handler = getClassHandlerForConversor(SongRef.class);
                staticlist.addSong((SongRef) handler.convertFromPersistenceModelToClientModel(st));
            }
        }
        return staticlist;
    }

    /**
	 * 
	 */
    @Override
    public StaticListType convertFromClientModelToPersistenceModel(Object obj) {
        StaticList staticlist = (StaticList) obj;
        StaticListType sltype = xmlTypesFactory.createStaticListType();
        sltype.setName(staticlist.getName());
        ListIterator<SongRef> li = staticlist.listIterator();
        while (li.hasNext()) {
            SongRef songref = li.next();
            AbstractConversionHandler handler = getClassHandlerForConversor(songref);
            sltype.addSong((SongType) handler.convertFromClientModelToPersistenceModel(songref));
        }
        return sltype;
    }
}
