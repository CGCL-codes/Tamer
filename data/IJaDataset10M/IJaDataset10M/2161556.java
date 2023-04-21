package net.sourceforge.arcamplayer.protmoddis.core;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.ClassificationComponent;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.ClassificationComposite;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.Collection;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.FilterRule;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.Folder;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.MediaPool;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.SmartList;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.Song;
import net.sourceforge.arcamplayer.protmoddis.mediaCollection.StaticList;
import net.sourceforge.arcamplayer.protmoddis.xmlhandlers.DiscotecaConversionHandler;
import net.sourceforge.arcamplayer.protmoddis.xmltypes.DiscotecaType;
import net.sourceforge.arcamplayer.protmoddis.xmltypes.FolderType;
import net.sourceforge.arcamplayer.protmoddis.xmltypes.HierarchyTreeType;
import net.sourceforge.arcamplayer.protmoddis.xmltypes.ObjectFactory;
import net.sourceforge.arcamplayer.protmoddis.xmltypes.SongType;
import net.sourceforge.arcamplayer.protmoddis.xmltypes.SongsPoolType;
import net.sourceforge.arcamplayer.protmoddis.xmltypes.StaticListType;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {

    private static int id = 1;

    private static ObjectFactory factory;

    private static DiscotecaType dc;

    private static Logger logger;

    static {
        logger = Logger.getLogger(Main.class);
        PropertyConfigurator.configure("log4j.properties");
    }

    private Collection discoCollection;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        logger.debug("Iniciando ejecución del prototipo: main()");
        logger.debug("Iniciando creación de Disco Type");
        createAndMarshallDiscoType();
        dc = unmarshall();
        dc.getCreationDate();
        Main main = new Main();
        main.createACollection();
        main.marshallDiscoCollection();
        logger.debug("FINALIZANDO ejecución del prototipo");
    }

    private static void createAndMarshallDiscoType() {
        factory = new ObjectFactory();
        logger.debug("Creando DiscotecaType");
        DiscotecaType discoCollection;
        SongType s[] = new SongType[3];
        discoCollection = factory.createDiscotecaType();
        s[0] = factory.createSongType();
        s[0].setAlbum("The Wall");
        s[0].setTitle("Another Brick In The Wall, Part II");
        s[0].setAutor("Pink Floyd");
        s[0].setId(String.valueOf(id++));
        s[1] = factory.createSongType();
        s[1].setAlbum("The Wall");
        s[1].setTitle("Goodbye Blue Sky");
        s[1].setAutor("Pink Floyd");
        s[1].setId(String.valueOf(id++));
        s[2] = factory.createSongType();
        s[2].setAlbum("IV");
        s[2].setTitle("Stairway To Heaven");
        s[2].setAutor("Led Zeppelin");
        s[2].setId(String.valueOf(id++));
        logger.debug("Añadiendo carpetas");
        FolderType folder = factory.createFolderType();
        folder.setName("David's Collection");
        FolderType frock = factory.createFolderType();
        frock.setName("Hard Rock");
        folder.getFolder().add(frock);
        HierarchyTreeType tree = factory.createHierarchyTreeType();
        tree.setRoot(folder);
        folder.getFolder().add(frock);
        logger.debug("Añadiendo canciones y listas");
        StaticListType staticlist = factory.createStaticListType();
        staticlist.setName("Rock PF");
        staticlist.getSong().add(factory.createStaticListTypeSong(s[0]));
        staticlist.getSong().add(factory.createStaticListTypeSong(s[1]));
        frock.getStaticList().add(staticlist);
        staticlist = factory.createStaticListType();
        staticlist.setName("Rock LZ");
        staticlist.getSong().add(factory.createStaticListTypeSong(s[2]));
        frock.getStaticList().add(staticlist);
        SongsPoolType pool = factory.createSongsPoolType();
        pool.getSong().add(s[0]);
        pool.getSong().add(s[1]);
        pool.getSong().add(s[2]);
        discoCollection.setHierarchyTree(tree);
        discoCollection.setSongsPool(pool);
        logger.debug("Finalizando DiscotecaType");
        marshall(discoCollection);
    }

    @SuppressWarnings("unchecked")
    private static DiscotecaType unmarshall() {
        logger.debug("Iniciando Unmarshalling");
        String packageName = DiscotecaType.class.getPackage().getName();
        JAXBContext jc;
        JAXBElement<DiscotecaType> doc = null;
        try {
            jc = JAXBContext.newInstance(packageName);
            logger.debug("Creando Marshaller");
            Unmarshaller u = jc.createUnmarshaller();
            logger.debug("Deserializando");
            JAXBElement<DiscotecaType> unmarshal = (JAXBElement<DiscotecaType>) u.unmarshal(new File("entrada.xml"));
            doc = unmarshal;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        logger.debug("Finalizado Unmarshalling");
        return (doc == null ? null : doc.getValue());
    }

    private void marshallDiscoCollection() {
        logger.debug("Iniciando conversión de la estructura de datos de proceso en Xml Types");
        DiscotecaConversionHandler conv = new DiscotecaConversionHandler();
        DiscotecaType dType = conv.convert2XmlType(discoCollection);
        logger.debug("Conversión finalizada");
        marshall(dType, "disco.xml");
    }

    private static void marshall(DiscotecaType collection, String filename) {
        logger.debug("Iniciando Marshalling de DiscotecaType en " + filename);
        JAXBElement<DiscotecaType> doc = factory.createDiscoteca(collection);
        try {
            JAXBContext context = JAXBContext.newInstance(collection.getClass().getPackage().getName());
            logger.debug("Creando Marshaller");
            Marshaller marshaller = context.createMarshaller();
            logger.debug("Serializando");
            marshaller.marshal(doc, new File(filename));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        logger.debug("Finalizado Marshalling");
    }

    private static void marshall(DiscotecaType collection) {
        marshall(collection, "salida.xml");
    }

    public void createACollection() {
        if (factory == null) {
            factory = new ObjectFactory();
        }
        logger.debug("Creando una discoteca en la estructura principal");
        Song s[] = new Song[15];
        discoCollection = new Collection();
        s[0] = new Song();
        s[0].setAlbum("The Wall");
        s[0].setTitle("Another Brick In The Wall, Part II");
        s[0].setAutor("Pink Floyd");
        s[0].setId(Collection.generateId());
        s[1] = new Song();
        s[1].setAlbum("The Wall");
        s[1].setTitle("Goodbye Blue Sky");
        s[1].setAutor("Pink Floyd");
        s[1].setId(Collection.generateId());
        s[2] = new Song();
        s[2].setAlbum("IV");
        s[2].setTitle("Stairway To Heaven");
        s[2].setAutor("Led Zeppelin");
        s[2].setId(Collection.generateId());
        s[3] = new Song();
        s[3].setAlbum("IV");
        s[3].setTitle("The Battle of Evermore");
        s[3].setAutor("Led Zeppelin");
        s[3].setId(Collection.generateId());
        s[4] = new Song();
        s[4].setAlbum("IV");
        s[4].setTitle("When The Levee Breaks");
        s[4].setAutor("Led Zeppelin");
        s[4].setId(Collection.generateId());
        s[5] = new Song();
        s[5].setAlbum("Nose");
        s[5].setTitle("In the Army");
        s[5].setAutor("Status Quo");
        s[5].setId(Collection.generateId());
        s[6] = new Song();
        s[6].setAlbum("The Forty Licks");
        s[6].setTitle("Paint It Black");
        s[6].setAutor("The Rolling Stones");
        s[6].setId(Collection.generateId());
        s[7] = new Song();
        s[7].setAlbum("Born To Live");
        s[7].setTitle("Badlands");
        s[7].setAutor("Broce Springsteen and The E Street Band");
        s[7].setId(Collection.generateId());
        s[8] = new Song();
        s[8].setAlbum("ElAlbum");
        s[8].setTitle("Serenade From The Stars");
        s[8].setAutor("Steve Miller Band");
        s[8].setId(Collection.generateId());
        s[9] = new Song();
        s[9].setAlbum("In the Air");
        s[9].setTitle("Sweet Home Alabama");
        s[9].setAutor("Lynyrd Skynyrd");
        s[9].setId(Collection.generateId());
        s[10] = new Song();
        s[10].setAlbum("Layla LP");
        s[10].setTitle("Layla");
        s[10].setAutor("Eric Clapton");
        s[10].setId(Collection.generateId());
        s[11] = new Song();
        s[11].setAlbum("A-ha");
        s[11].setTitle("Take On Me");
        s[11].setAutor("A-ha");
        s[11].setId(Collection.generateId());
        s[12] = new Song();
        s[12].setAlbum("Strange Days");
        s[12].setTitle("People Are Stange");
        s[12].setAutor("The Doors");
        s[12].setId(Collection.generateId());
        s[13] = new Song();
        s[13].setAlbum("On The Road");
        s[13].setTitle("Hotel California");
        s[13].setAutor("The Eagles");
        s[13].setId(Collection.generateId());
        s[14] = new Song();
        s[14].setAlbum("Made In Japan");
        s[14].setTitle("Smoke On The Water");
        s[14].setAutor("Deep Purple");
        s[14].setId(Collection.generateId());
        Folder folder = new Folder("David's Collection");
        Folder frock = new Folder("Hard Rock");
        Folder frband = new Folder("Bandas");
        folder.add(frock);
        frock.add(frband);
        discoCollection.getHierarchyTree().setRoot(new Folder("ROOT"));
        discoCollection.getHierarchyTree().getRoot().add(folder);
        StaticList staticlist = new StaticList("Pink Floyd");
        staticlist.addSong(s[0]);
        staticlist.addSong(s[1]);
        frband.add(staticlist);
        staticlist = new StaticList("Led Zeppelin");
        staticlist.addSong(s[2]);
        staticlist.addSong(s[3]);
        staticlist.addSong(s[4]);
        frband.add(staticlist);
        staticlist = new StaticList("Variado");
        staticlist.addSong(s[5]);
        staticlist.addSong(s[6]);
        staticlist.addSong(s[7]);
        staticlist.addSong(s[8]);
        staticlist.addSong(s[9]);
        staticlist.addSong(s[10]);
        staticlist.addSong(s[11]);
        staticlist.addSong(s[12]);
        staticlist.addSong(s[13]);
        staticlist.addSong(s[14]);
        frock.add(staticlist);
        SmartList smart = new SmartList("AUTO: Gn'R");
        FilterRule rule = new FilterRule();
        rule.setField("Autor");
        rule.setOperation("LIKE");
        rule.setValue("Guns N' Roses");
        smart.addFilterRule(rule);
        frock.add(smart);
        smart = new SmartList("AUTO: Queen Early Hits");
        frock.add(smart);
        rule = new FilterRule();
        rule.setField("Autor");
        rule.setOperation("LIKE");
        rule.setValue("Queen");
        smart.addFilterRule(rule);
        rule = new FilterRule();
        rule.setField("Year");
        rule.setOperation(">=");
        rule.setValue("1970");
        smart.addFilterRule(rule);
        rule = new FilterRule();
        rule.setField("Year");
        rule.setOperation("<=");
        rule.setValue("1980");
        smart.addFilterRule(rule);
        MediaPool pool = discoCollection.getSongsPool();
        pool.addSong(s[0]);
        pool.addSong(s[1]);
        pool.addSong(s[2]);
        pool.addSong(s[3]);
        pool.addSong(s[4]);
        pool.addSong(s[5]);
        pool.addSong(s[6]);
        pool.addSong(s[7]);
        pool.addSong(s[8]);
        pool.addSong(s[9]);
        pool.addSong(s[10]);
        pool.addSong(s[11]);
        pool.addSong(s[12]);
        pool.addSong(s[13]);
        pool.addSong(s[14]);
        logger.debug("Discoteca creada");
    }
}
