package com.dyuproject.protostuff.benchmark.serializers;

import com.dyuproject.protostuff.benchmark.V2SpeedMedia.Image;
import com.dyuproject.protostuff.benchmark.V2SpeedMedia.Media;
import com.dyuproject.protostuff.benchmark.V2SpeedMedia.MediaContent;
import com.dyuproject.protostuff.benchmark.V2SpeedMedia.Image.Size;
import com.dyuproject.protostuff.benchmark.V2SpeedMedia.Media.Player;

/**
 * @author David Yu
 * @created Oct 16, 2009
 */
public abstract class AbstractSpeedMediaSerializer implements ObjectSerializer<MediaContent> {

    public MediaContent create() {
        MediaContent contentProto = MediaContent.newBuilder().setMedia(Media.newBuilder().setFormat("video/mpg4").setPlayer(Player.JAVA).setTitle("Javaone Keynote").setUri("http://javaone.com/keynote.mpg").setDuration(1234567).setSize(123).setHeight(0).setWidth(0).setBitrate(123).addPerson("Bill Gates").addPerson("Steve Jobs").build()).addImage(Image.newBuilder().setHeight(0).setTitle("Javaone Keynote").setUri("http://javaone.com/keynote_large.jpg").setWidth(0).setSize(Size.LARGE).build()).addImage(Image.newBuilder().setHeight(0).setTitle("Javaone Keynote").setUri("http://javaone.com/keynote_thumbnail.jpg").setWidth(0).setSize(Size.SMALL).build()).build();
        return contentProto;
    }
}
