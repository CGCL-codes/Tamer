package de.beas.explicanto.screenplay;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import de.beas.explicanto.screenplay.jaxb.Character;
import de.beas.explicanto.screenplay.jaxb.Direction;
import de.beas.explicanto.screenplay.jaxb.Frame;
import de.beas.explicanto.screenplay.jaxb.Location;
import de.beas.explicanto.screenplay.jaxb.Member;
import de.beas.explicanto.screenplay.jaxb.Occurence;
import de.beas.explicanto.screenplay.jaxb.Scene;
import de.beas.explicanto.screenplay.jaxb.Screenplay;
import de.beas.explicanto.screenplay.jaxb.TimeOfDay;
import de.beas.explicanto.screenplay.jaxb.impl.CharacterImpl;
import de.beas.explicanto.screenplay.jaxb.impl.DirectionImpl;
import de.beas.explicanto.screenplay.jaxb.impl.FrameImpl;
import de.beas.explicanto.screenplay.jaxb.impl.LocationImpl;
import de.beas.explicanto.screenplay.jaxb.impl.MemberImpl;
import de.beas.explicanto.screenplay.jaxb.impl.OccurenceImpl;
import de.beas.explicanto.screenplay.jaxb.impl.SceneImpl;
import de.beas.explicanto.screenplay.jaxb.impl.ScreenplayImpl;
import de.beas.explicanto.screenplay.jaxb.impl.TimeOfDayImpl;
import de.beas.explicanto.types.WSCharacter;
import de.beas.explicanto.types.WSDirection;
import de.beas.explicanto.types.WSFrame;
import de.beas.explicanto.types.WSLocation;
import de.beas.explicanto.types.WSMember;
import de.beas.explicanto.types.WSOccurence;
import de.beas.explicanto.types.WSScene;
import de.beas.explicanto.types.WSScreenPlayObject;
import de.beas.explicanto.types.WSScreenplay;
import de.beas.explicanto.types.WSTimeOfDay;

/**
 * Converts between server data type and the client data type
 * 
 * @author marius.staicu
 * 
 */
public class JAXBConverter {

    private static Marshaller marsh;

    private static Unmarshaller unmarsh;

    static {
        try {
            JAXBContext ctx = JAXBContext.newInstance(Screenplay.class.getPackage().getName());
            marsh = ctx.createMarshaller();
            unmarsh = ctx.createUnmarshaller();
        } catch (Exception e) {
            throw new RuntimeException("could not initialize JAXBConverter", e);
        }
    }

    /**
	 * Converts the server screenplay data structure to the client data
	 * structure
	 * 
	 * @param ws
	 * @return
	 * @throws JAXBException
	 */
    public static WSScreenPlayObject convertScreenplay(WSScreenplay ws) throws JAXBException {
        WSScreenPlayObject data = new WSScreenPlayObject();
        data.setUid(ws.getUid());
        data.setTableTitle(ws.getTitle());
        data.setLockUserId(ws.getLockUserID());
        data.setLockTime(ws.getLockTime());
        data.setLockCount(ws.getLockCount());
        Screenplay sc = ((Screenplay) unmarsh.unmarshal(new StreamSource(new StringReader(ws.getContent()))));
        data.setAnimationResource(sc.getAnimationResource());
        data.setLanguage(sc.getLanguage());
        data.setMediaResource(sc.getMediaResource());
        data.setObjective(sc.getObjective());
        data.setStatus(sc.getStatus());
        data.setStory(sc.getStory());
        data.setTargetGroup(sc.getTargetGroup());
        data.setTitle(sc.getTitle());
        List list = data.getCast();
        for (Iterator i = sc.getCast().iterator(); i.hasNext(); ) list.add(convertMember((Member) i.next()));
        list = data.getCharacters();
        for (Iterator i = sc.getCharacters().iterator(); i.hasNext(); ) list.add(convertCharacter((Character) i.next()));
        list = data.getLocations();
        for (Iterator i = sc.getLocations().iterator(); i.hasNext(); ) list.add(convertLocation((Location) i.next()));
        list = data.getScenes();
        for (Iterator i = sc.getScenes().iterator(); i.hasNext(); ) list.add(convertScene((Scene) i.next()));
        list = data.getTimeOfDays();
        for (Iterator i = sc.getTimeOfDays().iterator(); i.hasNext(); ) list.add(convertTimeOfDay((TimeOfDay) i.next()));
        return data;
    }

    private static TimeOfDay convertTimeOfDay(WSTimeOfDay day) {
        TimeOfDay result = new TimeOfDayImpl();
        result.setId(day.getId());
        result.setInformation(day.getInfo());
        result.setName(day.getName());
        return result;
    }

    private static WSTimeOfDay convertTimeOfDay(TimeOfDay day) {
        WSTimeOfDay result = new WSTimeOfDay();
        result.setId(day.getId());
        result.setInfo(day.getInformation());
        result.setName(day.getName());
        return result;
    }

    /**
	 * Converts the client data type to the server data type
	 * 
	 * @param data
	 * @return
	 * @throws JAXBException
	 */
    public static WSScreenplay convertScreenplay(WSScreenPlayObject data) throws JAXBException {
        WSScreenplay ws = new WSScreenplay();
        ws.setUid(data.getUid());
        ws.setTitle(data.getTableTitle());
        ws.setLockUserID(data.getLockUserId());
        ws.setLockTime(data.getLockTime());
        ws.setLockCount(data.getLockCount());
        StringWriter sw = new StringWriter();
        Screenplay sc = new ScreenplayImpl();
        sc.setAnimationResource(data.getAnimationResource());
        sc.setLanguage(data.getLanguage());
        sc.setMediaResource(data.getMediaResource());
        sc.setObjective(data.getObjective());
        sc.setStatus(data.getStatus());
        sc.setStory(data.getStory());
        sc.setTargetGroup(data.getTargetGroup());
        sc.setTitle(data.getTitle());
        List list = sc.getCast();
        for (Iterator i = data.getCast().iterator(); i.hasNext(); ) list.add(convertMember((WSMember) i.next()));
        list = sc.getCharacters();
        for (Iterator i = data.getCharacters().iterator(); i.hasNext(); ) list.add(convertCharacter((WSCharacter) i.next()));
        list = sc.getLocations();
        for (Iterator i = data.getLocations().iterator(); i.hasNext(); ) list.add(convertLocation((WSLocation) i.next()));
        list = sc.getTimeOfDays();
        for (Iterator i = data.getTimeOfDays().iterator(); i.hasNext(); ) list.add(convertTimeOfDay((WSTimeOfDay) i.next()));
        list = sc.getScenes();
        for (Iterator i = data.getScenes().iterator(); i.hasNext(); ) list.add(convertScene((WSScene) i.next()));
        marsh.marshal(sc, sw);
        ws.setContent(sw.toString());
        return ws;
    }

    /**
	 * Converts the client data type to the server data type
	 * 
	 * @param data
	 * @return
	 * @throws JAXBException
	 */
    public static Screenplay convertScreenplayNoWS(WSScreenPlayObject data) {
        WSScreenplay ws = new WSScreenplay();
        ws.setUid(data.getUid());
        ws.setTitle(data.getTableTitle());
        ws.setLockUserID(data.getLockUserId());
        ws.setLockTime(data.getLockTime());
        ws.setLockCount(data.getLockCount());
        StringWriter sw = new StringWriter();
        Screenplay sc = new ScreenplayImpl();
        sc.setAnimationResource(data.getAnimationResource());
        sc.setLanguage(data.getLanguage());
        sc.setMediaResource(data.getMediaResource());
        sc.setObjective(data.getObjective());
        sc.setStatus(data.getStatus());
        sc.setStory(data.getStory());
        sc.setTargetGroup(data.getTargetGroup());
        sc.setTitle(data.getTitle());
        List list = sc.getCast();
        for (Iterator i = data.getCast().iterator(); i.hasNext(); ) list.add(convertMember((WSMember) i.next()));
        list = sc.getCharacters();
        for (Iterator i = data.getCharacters().iterator(); i.hasNext(); ) list.add(convertCharacter((WSCharacter) i.next()));
        list = sc.getLocations();
        for (Iterator i = data.getLocations().iterator(); i.hasNext(); ) list.add(convertLocation((WSLocation) i.next()));
        list = sc.getTimeOfDays();
        for (Iterator i = data.getTimeOfDays().iterator(); i.hasNext(); ) list.add(convertTimeOfDay((WSTimeOfDay) i.next()));
        list = sc.getScenes();
        for (Iterator i = data.getScenes().iterator(); i.hasNext(); ) list.add(convertScene((WSScene) i.next()));
        return sc;
    }

    private static Scene convertScene(WSScene scene) {
        Scene result = new SceneImpl();
        result.setCode(scene.getCode());
        result.setLocation(scene.getLocation());
        result.setTimeOfDay(scene.getTimeOfDay());
        result.setName(scene.getName());
        result.setOrder(scene.getOrder());
        result.setPlot(scene.getPlot());
        result.setUid(scene.getUid());
        List list = result.getDirections();
        for (Iterator i = scene.getDirections().iterator(); i.hasNext(); ) list.add(convertDirection((WSDirection) i.next()));
        list = result.getOccurences();
        for (Iterator i = scene.getOccourences().iterator(); i.hasNext(); ) list.add(convertOccurence((WSOccurence) i.next()));
        list = result.getTimelines();
        for (Iterator i = scene.getTimelines().iterator(); i.hasNext(); ) list.add(convertFrame((WSFrame) i.next()));
        return result;
    }

    private static WSScene convertScene(Scene scene) {
        WSScene result = new WSScene();
        result.setCode(scene.getCode());
        result.setLocation(scene.getLocation());
        result.setName(scene.getName());
        result.setOrder(scene.getOrder());
        result.setPlot(scene.getPlot());
        result.setTimeOfDay(scene.getTimeOfDay());
        result.setUid(scene.getUid());
        List list = result.getTimelines();
        for (Iterator i = scene.getTimelines().iterator(); i.hasNext(); ) list.add(convertFrame((Frame) i.next()));
        list = result.getDirections();
        for (Iterator i = scene.getDirections().iterator(); i.hasNext(); ) list.add(convertDirection((Direction) i.next()));
        list = result.getOccourences();
        for (Iterator i = scene.getOccurences().iterator(); i.hasNext(); ) list.add(convertOccurence((Occurence) i.next()));
        return result;
    }

    private static Occurence convertOccurence(WSOccurence occurence) {
        Occurence result = new OccurenceImpl();
        result.setCastMember(occurence.getCastMember());
        result.setSpan(occurence.getSpan());
        result.setStartFrame(occurence.getStartFrame());
        return result;
    }

    private static WSOccurence convertOccurence(Occurence occurence) {
        WSOccurence result = new WSOccurence();
        result.setCastMember(occurence.getCastMember());
        result.setSpan(occurence.getSpan());
        result.setStartFrame(occurence.getStartFrame());
        return result;
    }

    private static Direction convertDirection(WSDirection direction) {
        Direction result = new DirectionImpl();
        result.setSpan(direction.getSpan());
        result.setStartFrame(direction.getStartFrame());
        result.setText(direction.getText());
        return result;
    }

    private static WSDirection convertDirection(Direction direction) {
        WSDirection result = new WSDirection();
        result.setSpan(direction.getSpan());
        result.setStartFrame(direction.getStartFrame());
        result.setText(direction.getText());
        return result;
    }

    private static Frame convertFrame(WSFrame frame) {
        Frame result = new FrameImpl();
        result.setUid(frame.getUid());
        return result;
    }

    private static WSFrame convertFrame(Frame frame) {
        WSFrame result = new WSFrame();
        result.setUid(frame.getUid());
        return result;
    }

    private static Location convertLocation(WSLocation location) {
        Location result = new LocationImpl();
        result.setId(location.getId());
        result.setInformation(location.getInfo());
        result.setName(location.getName());
        return result;
    }

    private static WSLocation convertLocation(Location location) {
        WSLocation result = new WSLocation();
        result.setId(location.getId());
        result.setInfo(location.getInformation());
        result.setName(location.getName());
        return result;
    }

    private static Character convertCharacter(WSCharacter character) {
        Character result = new CharacterImpl();
        result.setAge(character.getAge());
        result.setClothing(character.getClothing());
        result.setColor(character.getColor());
        result.setContribution(character.getContribution());
        result.setFacialExpression(character.getFacialExpression());
        result.setGestures(character.getGestures());
        result.setLanguage(character.getLanguage());
        result.setName(character.getName());
        result.setNickname(character.getNickname());
        result.setRole(character.getRole());
        result.setSex(character.getSex());
        result.setSize(character.getSize());
        result.setUid(character.getUid());
        return result;
    }

    private static WSCharacter convertCharacter(Character character) {
        WSCharacter result = new WSCharacter();
        result.setAge(character.getAge());
        result.setClothing(character.getClothing());
        result.setColor(character.getColor());
        result.setContribution(character.getContribution());
        result.setFacialExpression(character.getFacialExpression());
        result.setGestures(character.getGestures());
        result.setLanguage(character.getLanguage());
        result.setName(character.getName());
        result.setNickname(character.getNickname());
        result.setRole(character.getRole());
        result.setSex(character.getSex());
        result.setSize(character.getSize());
        result.setUid(character.getUid());
        return result;
    }

    private static Member convertMember(WSMember member) {
        Member result = new MemberImpl();
        result.setAudioFile(member.getAudioFile());
        result.setCharacterId(member.getCharacterID());
        result.setDescription(member.getDescription());
        result.setImage(member.getImage());
        result.setMemberClass(member.getMemberClass());
        result.setName(member.getName());
        result.setPersonal(member.getPersonal());
        result.setShortScript(member.getShortScript());
        result.setStatus(member.getStatus());
        result.setUid(member.getUid());
        return result;
    }

    private static WSMember convertMember(Member member) {
        WSMember result = new WSMember();
        result.setAudioFile(member.getAudioFile());
        result.setCharacterID(member.getCharacterId());
        result.setDescription(member.getDescription());
        result.setImage(member.getImage());
        result.setMemberClass(member.getMemberClass());
        result.setName(member.getName());
        result.setPersonal(member.getPersonal());
        result.setShortScript(member.getShortScript());
        result.setStatus(member.getStatus());
        result.setUid(member.getUid());
        return result;
    }
}
