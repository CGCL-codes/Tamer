package com.restfb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Unit tests that exercise {@link JsonMapper} implementations, specifically the
 * "convert Java to JSON" functionality.
 * 
 * @author <a href="http://restfb.com">Mark Allen</a>
 */
public class JsonMapperToJsonTest extends AbstractJsonMapperTests {

    /**
   * Can we handle null?
   */
    @Test
    public void nullObject() {
        String json = createJsonMapper().toJson(null);
        Assert.assertTrue("null".equals(json));
    }

    /**
   * Can we handle the empty list?
   */
    @Test
    public void emptyList() {
        String json = createJsonMapper().toJson(new ArrayList<Object>());
        Assert.assertTrue("[]".equals(json));
    }

    /**
   * Can we handle the empty object?
   */
    @Test
    public void emptyObject() {
        String json = createJsonMapper().toJson(new Object());
        Assert.assertTrue("{}".equals(json));
    }

    /**
   * Can we handle primitives?
   */
    @Test
    public void primitives() {
        Assert.assertTrue("Testing".equals(createJsonMapper().toJson("Testing")));
        Assert.assertTrue("true".equals(createJsonMapper().toJson(true)));
        Assert.assertTrue("1".equals(createJsonMapper().toJson(1)));
        Assert.assertTrue("1".equals(createJsonMapper().toJson(1L)));
        Assert.assertTrue("1.0".equals(createJsonMapper().toJson(1F)));
        Assert.assertTrue("1.0".equals(createJsonMapper().toJson(1D)));
        Assert.assertTrue("1".equals(createJsonMapper().toJson(new BigInteger("1"))));
        Assert.assertTrue("1.0".equals(createJsonMapper().toJson(new BigDecimal("1"))));
    }

    /**
   * Can we handle a basic Javabean?
   */
    @Test
    public void basicJavabean() {
        BasicUser basicUser = new BasicUser();
        basicUser.uid = 12345L;
        basicUser.name = "Fred";
        String json = createJsonMapper().toJson(basicUser);
        Assert.assertTrue("{\"uid\":12345,\"name\":\"Fred\"}".equals(json));
    }

    /**
   * Can we handle a more complex Javabean?
   */
    @Test
    public void complexJavabean() {
        UserWithPhotos userWithPhotos = new UserWithPhotos();
        userWithPhotos.uid = 12345L;
        userWithPhotos.name = null;
        userWithPhotos.photos = new ArrayList<Photo>();
        userWithPhotos.photos.add(new Photo());
        Photo photo = new Photo();
        photo.photoId = 5678L;
        photo.location = "Las Vegas";
        userWithPhotos.photos.add(photo);
        String json = createJsonMapper().toJson(userWithPhotos);
        Assert.assertTrue("{\"uid\":12345,\"photos\":[{\"id\":null,\"location\":null},{\"id\":5678,\"location\":\"Las Vegas\"}],\"name\":null}".equals(json));
    }

    /**
   * Can we handle a full stream.publish example?
   * <p>
   * See http://wiki.developers.facebook.com/index.php/Attachment_(Streams).
   */
    @Test
    public void streamPublish() {
        ActionLink category = new ActionLink();
        category.href = "http://bit.ly/KYbaN";
        category.text = "humor";
        Properties properties = new Properties();
        properties.category = category;
        properties.ratings = "5 stars";
        Medium medium = new Medium();
        medium.href = "http://bit.ly/187gO1";
        medium.src = "http://icanhascheezburger.files.wordpress.com/2009/03/funny-pictures-your-cat-is-bursting-with-joy1.jpg";
        medium.type = "image";
        List<Medium> media = new ArrayList<Medium>();
        media.add(medium);
        Attachment attachment = new Attachment();
        attachment.name = "i'm bursting with joy";
        attachment.href = "http://bit.ly/187gO1";
        attachment.caption = "{*actor*} rated the lolcat 5 stars";
        attachment.description = "a funny looking cat";
        attachment.properties = properties;
        attachment.media = media;
        String json = createJsonMapper().toJson(attachment);
        Assert.assertTrue("{\"description\":\"a funny looking cat\",\"name\":\"i'm bursting with joy\",\"caption\":\"{*actor*} rated the lolcat 5 stars\",\"properties\":{\"category\":{\"text\":\"humor\",\"href\":\"http://bit.ly/KYbaN\"},\"ratings\":\"5 stars\"},\"media\":[{\"src\":\"http://icanhascheezburger.files.wordpress.com/2009/03/funny-pictures-your-cat-is-bursting-with-joy1.jpg\",\"type\":\"image\",\"href\":\"http://bit.ly/187gO1\"}],\"href\":\"http://bit.ly/187gO1\"}".equals(json));
    }

    /**
   * Can we handle an empty Map?
   */
    @Test
    public void emptyMap() {
        Assert.assertTrue("{}".equals(createJsonMapper().toJson(new HashMap<String, Object>())));
    }

    /**
   * Can we handle a Map?
   */
    @Test
    public void map() {
        UserWithPhotos basicUser = new UserWithPhotos();
        basicUser.uid = 12345L;
        basicUser.name = "Fred";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("testId", new BigInteger("412"));
        map.put("floatId", Float.valueOf(123.45F));
        map.put("basicUser", basicUser);
        String json = createJsonMapper().toJson(map);
        Assert.assertTrue("{\"floatId\":123.45,\"testId\":412,\"basicUser\":{\"uid\":12345,\"photos\":null,\"name\":\"Fred\"}}".equals(json));
    }

    static class BasicUser {

        @Facebook
        Long uid;

        @Facebook
        String name;

        Byte ignored;
    }

    static class Photo {

        @Facebook("id")
        Long photoId;

        @Facebook
        String location;
    }

    static class UserWithPhotos extends BasicUser {

        @Facebook
        List<Photo> photos;
    }

    static class ActionLink {

        @Facebook
        String text;

        @Facebook
        String href;
    }

    static class Medium {

        @Facebook
        String type;

        @Facebook
        String src;

        @Facebook
        String href;
    }

    static class Properties {

        @Facebook
        ActionLink category;

        @Facebook
        String ratings;
    }

    static class Attachment {

        @Facebook
        String name;

        @Facebook
        String href;

        @Facebook
        String caption;

        @Facebook
        String description;

        @Facebook
        Properties properties;

        @Facebook
        List<Medium> media;
    }
}
