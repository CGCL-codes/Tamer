package net.jmarias.uqueue.resources;

import net.jmarias.properties.*;
import net.jmarias.uqueue.contact.MediaType;

/**
 *
 * @author jose
 */
public class ConcreteMediaSet extends ConcreteResource implements MediaSet, PropertiesSubject {

    Integer voice_voice = 0;

    Integer voice_video = 0;

    Integer voice_chat = 0;

    Integer voice_email = 0;

    Integer video_voice = 0;

    Integer video_video = 0;

    Integer video_chat = 0;

    Integer video_email = 0;

    Integer chat_voice = 0;

    Integer chat_video = 0;

    Integer chat_chat = 0;

    Integer chat_email = 0;

    Integer email_voice = 0;

    Integer email_video = 0;

    Integer email_chat = 0;

    Integer email_email = 0;

    public static MediaSet getInstance(Integer resId, String name, String desc, Integer subDomainId) {
        return new ConcreteMediaSet(resId, name, desc, subDomainId);
    }

    private ConcreteMediaSet(Integer resId, String name, String desc, Integer subDomainId) {
        super(resId, name, desc, subDomainId, ResourceType.MEDIASET);
    }

    @Override
    public Boolean canDealWithMedia(MediaType newMedia, Integer voice, Integer video, Integer chat, Integer email) {
        if (newMedia.equals(MediaType.VOICE)) {
            return ((voice <= voice_voice) && (video <= voice_video) && (chat <= voice_chat) && (voice_voice <= voice));
        } else if (newMedia.equals(MediaType.VIDEO)) {
            return ((voice <= video_voice) && (video <= video_video) && (chat <= video_chat) && (video_voice <= voice));
        } else if (newMedia.equals(MediaType.CHAT)) {
            return ((voice <= chat_voice) && (video <= chat_video) && (chat <= chat_chat) && (chat_voice <= voice));
        } else if (newMedia.equals(MediaType.EMAIL)) {
            return ((voice <= email_voice) && (video <= email_video) && (chat <= email_chat) && (email_voice <= voice));
        }
        return false;
    }

    @Override
    public void setProperty(String property, String value) {
        super.setProperty(property, value);
        if (property.compareToIgnoreCase("voice_voice") == 0) {
            voice_voice = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("voice_video") == 0) {
            voice_video = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("voice_chat") == 0) {
            voice_chat = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("voice_email") == 0) {
            voice_email = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("video_voice") == 0) {
            video_voice = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("video_video") == 0) {
            video_video = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("video_chat") == 0) {
            video_chat = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("video_email") == 0) {
            video_email = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("chat_voice") == 0) {
            chat_voice = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("chat_video") == 0) {
            chat_video = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("chat_chat") == 0) {
            chat_chat = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("chat_email") == 0) {
            chat_email = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("email_voice") == 0) {
            email_voice = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("email_video") == 0) {
            email_video = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("email_chat") == 0) {
            email_chat = Integer.parseInt(value);
        } else if (property.compareToIgnoreCase("email_email") == 0) {
            email_email = Integer.parseInt(value);
        }
    }
}
