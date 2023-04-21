package ch.intertec.storybook.testng.database;

import java.awt.Color;
import java.io.File;
import java.util.Calendar;
import java.util.Random;
import ch.intertec.storybook.model.Chapter;
import ch.intertec.storybook.model.Gender;
import ch.intertec.storybook.model.Item;
import ch.intertec.storybook.model.Tag;
import ch.intertec.storybook.model.TagLink;
import ch.intertec.storybook.model.Location;
import ch.intertec.storybook.model.SbCharacter;
import ch.intertec.storybook.model.Scene;
import ch.intertec.storybook.model.SceneLinkLocation;
import ch.intertec.storybook.model.SceneLinkSbCharacter;
import ch.intertec.storybook.model.SceneLinkStrand;
import ch.intertec.storybook.model.Strand;
import ch.intertec.storybook.model.Tag.TagType;

public abstract class AbstractDemo extends AbstractTest {

    protected static int sceneCounter = 0;

    protected static Random randomGenerator = new Random();

    protected abstract void createGenders() throws Exception;

    protected abstract void createCharacters() throws Exception;

    protected abstract void createLocations() throws Exception;

    protected abstract void createParts() throws Exception;

    protected abstract void createChapters() throws Exception;

    protected abstract void createStrands() throws Exception;

    protected abstract void createScenes() throws Exception;

    protected abstract void createTags() throws Exception;

    protected abstract void createItems() throws Exception;

    public void createObjects() throws Exception {
        createGenders();
        createCharacters();
        createLocations();
        createParts();
        createChapters();
        createStrands();
        createScenes();
        createTags();
        createItems();
    }

    protected static Scene makeScene(Calendar cal, Chapter chapter, Strand strand, String title, String summary) throws Exception {
        return makeScene(cal, chapter, strand, title, summary, Scene.Status.DONE.ordinal());
    }

    protected static Scene makeScene(Calendar cal, Chapter chapter, Strand strand, String title, String summary, int status) throws Exception {
        ++sceneCounter;
        Scene scene = new Scene();
        scene.setSceneNo(sceneCounter);
        scene.setChapter(chapter);
        scene.setStrand(strand);
        scene.setDate(cal);
        scene.setTitle(title);
        scene.setText(summary);
        scene.setStatus(status);
        scene.save();
        return scene;
    }

    protected static void makeLocationLink(Scene scene, Location location) throws Exception {
        SceneLinkLocation lnk = new SceneLinkLocation();
        lnk.setScene(scene);
        lnk.setLocation(location);
        lnk.save();
    }

    protected static void makeTagLink(Tag tag, Location location) throws Exception {
        TagLink lnk = new TagLink();
        lnk.setTag(tag);
        lnk.setLocation(location);
        lnk.save();
    }

    protected static void makeTagLink(Tag tag, SbCharacter character) throws Exception {
        TagLink lnk = new TagLink();
        lnk.setTag(tag);
        lnk.setCharacter(character);
        lnk.save();
    }

    protected static void makeTagLink(Tag tag, SbCharacter character, Scene startScene) throws Exception {
        makeTagLink(tag, character, startScene, null);
    }

    protected static void makeTagLink(Tag tag, SbCharacter character, Scene startScene, Scene endScene) throws Exception {
        TagLink lnk = new TagLink();
        lnk.setTag(tag);
        lnk.setCharacter(character);
        lnk.setStartScene(startScene);
        if (endScene != null) {
            lnk.setEndScene(endScene);
        }
        lnk.save();
    }

    protected static void makeTagLink(Tag tag, Scene scene) throws Exception {
        TagLink lnk = new TagLink();
        lnk.setTag(tag);
        lnk.setScene(scene);
        lnk.save();
    }

    protected static void makeTagLink(Tag tag, Scene startScene, Scene endScene) throws Exception {
        TagLink lnk = new TagLink();
        lnk.setTag(tag);
        lnk.setStartScene(startScene);
        lnk.setEndScene(endScene);
        lnk.save();
    }

    protected static void makeCharacterLink(Scene scene, SbCharacter character) throws Exception {
        SceneLinkSbCharacter lnk = new SceneLinkSbCharacter();
        lnk.setScene(scene);
        lnk.setCharacter(character);
        lnk.save();
    }

    protected static void makeStrandLink(Scene scene, Strand strand) throws Exception {
        SceneLinkStrand lnk = new SceneLinkStrand();
        lnk.setScene(scene);
        lnk.setStrand(strand);
        lnk.save();
    }

    protected static Tag makeTag(String name, String category, String descr) throws Exception {
        Tag tag = new Tag();
        tag.setTagType(TagType.TAG);
        tag.setName(name);
        tag.setCategory(category);
        tag.setDescription(descr);
        tag.setNotes("");
        tag.save();
        return tag;
    }

    protected static Item makeItem(String name, String category, String descr) throws Exception {
        Item item = new Item();
        item.setTagType(TagType.ITEM);
        item.setName(name);
        item.setCategory(category);
        item.setDescription(descr);
        item.setNotes("");
        item.save();
        return item;
    }

    protected static Location makeLocation(String name, String city, String country, String descr) throws Exception {
        Location location = new Location();
        location.setName(name);
        location.setCity(city);
        location.setCountry(country);
        location.setDescription(descr);
        location.save();
        return location;
    }

    protected static SbCharacter makeCharacter(String firstname, String lastname, String abbreviation, String description, Gender gender, String birthdayStr) throws Exception {
        return makeCharacter(firstname, lastname, abbreviation, description, gender, birthdayStr, null, true);
    }

    protected static SbCharacter makeCharacter(String firstname, String lastname, String abbreviation, String description, Gender gender, String birthdayStr, Color color, boolean central) throws Exception {
        SbCharacter character = new SbCharacter();
        character.setFirstname(firstname);
        character.setLastname(lastname);
        character.setAbbreviation(abbreviation);
        character.setDescription(description);
        character.setGender(gender);
        if (birthdayStr != null) {
            character.setBirthdayStr(birthdayStr);
        }
        character.setColor(color);
        if (central) {
            character.setCategory(SbCharacter.CATEGORY_CENTRAL);
        } else {
            character.setCategory(SbCharacter.CATEGORY_MINOR);
        }
        character.save();
        return character;
    }

    protected static Gender makeGender(String name, int childhood, int adolescence, int adulthood, int retirement) throws Exception {
        Gender gender = new Gender();
        gender.setName(name);
        gender.setChildhood(childhood);
        gender.setAdolescence(adolescence);
        gender.setAdulthood(adulthood);
        gender.setRetirement(retirement);
        gender.save();
        return gender;
    }

    public AbstractDemo() {
        super();
    }

    public AbstractDemo(File dir) {
        super(dir);
    }
}
