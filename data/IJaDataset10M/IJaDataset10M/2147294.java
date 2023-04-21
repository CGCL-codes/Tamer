package net.sourceforge.arcamplayer.protmoddis.xmlhandlers;

import java.util.ListIterator;
import javax.xml.bind.JAXBElement;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.Song;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.StaticList;
import net.sourceforge.arcamplayer.protmoddis.xmltypes.SongType;
import net.sourceforge.arcamplayer.protmoddis.xmltypes.StaticListType;

public class StaticListConversionHandler extends AbstractConversionHandler {

    @Override
    public StaticList convert2DataObject(Object obj) {
        StaticListType sltype = (StaticListType) obj;
        StaticList staticlist = new StaticList(sltype.getName());
        ListIterator<JAXBElement<Object>> li = sltype.listIterator();
        while (li.hasNext()) {
            SongType st = (SongType) ((JAXBElement<Object>) li.next()).getValue();
            AbstractConversionHandler handler = getClassHandler2Conv(st);
            staticlist.addSong((Song) handler.convert2DataObject(st));
        }
        return staticlist;
    }

    @Override
    public StaticListType convert2XmlType(Object obj) {
        StaticList staticlist = (StaticList) obj;
        StaticListType sltype = xmlTypesFactory.createStaticListType();
        sltype.setName(staticlist.getName());
        ListIterator<Song> li = staticlist.listIterator();
        while (li.hasNext()) {
            Song song = li.next();
            AbstractConversionHandler handler = getClassHandler2Conv(song);
            sltype.addSong((SongType) handler.convert2XmlType(song));
        }
        return sltype;
    }
}
