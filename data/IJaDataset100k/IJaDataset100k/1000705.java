package com.dyuproject.protostuff.benchmark.serializers;

import com.dyuproject.protostuff.benchmark.Image;
import com.dyuproject.protostuff.benchmark.Media;
import com.dyuproject.protostuff.benchmark.MediaContent;
import com.dyuproject.protostuff.benchmark.Image.Size;
import com.dyuproject.protostuff.benchmark.Media.Player;

/**
 * Base serializer for protostuff.
 *
 * @author David Yu
 * @created Jan 14, 2010
 */
public abstract class AbstractProtostuffSerializer implements ObjectSerializer<MediaContent> {

    public MediaContent create() {
        MediaContent mediaContent = new MediaContent(new Media("http://javaone.com/keynote.mpg", 0, 0, "video/mpg4", 1234567l, 123l, Player.JAVA).setTitle("Javaone Keynote").setBitrate(123).addPerson("Bill Gates").addPerson("Steve Jobs")).addImage(new Image("http://javaone.com/keynote_large.jpg", 0, 0, Size.LARGE).setTitle("Javaone Keynote")).addImage(new Image("http://javaone.com/keynote_thumbnail.jpg", 0, 0, Size.SMALL).setTitle("Javaone Keynote"));
        return mediaContent;
    }
}
