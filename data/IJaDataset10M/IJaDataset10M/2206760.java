package jblip.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jblip.gui.data.channels.DataChannel;
import jblip.gui.data.channels.UpdatesDataChannel;

public abstract class UpdateTag {

    protected static final String UPDATE_REGEX = "^http://(?:www\\.)?blip\\.pl/(?:s|statuses|dm|directed_messages|pm|private_messages)/(\\d+)$";

    private final String source_string;

    public static UpdateTag getTag(final String source_string) {
        if (source_string.length() <= 3) {
            return null;
        }
        try {
            if (source_string.startsWith("^")) {
                return new UserReference(source_string);
            } else if (source_string.startsWith("#")) {
                return new TagReference(source_string);
            } else if (source_string.matches(UPDATE_REGEX)) {
                return new UpdateLink(source_string);
            } else if (source_string.startsWith("http")) {
                return new ExternalLink(source_string);
            }
        } catch (IllegalArgumentException iae) {
            return null;
        }
        return null;
    }

    protected UpdateTag(final String source_string) {
        this.source_string = source_string;
    }

    public String toString() {
        return source_string;
    }

    public boolean hasChannel() {
        return true;
    }

    public abstract DataChannel<?> getChannel();

    public boolean hasURI() {
        return true;
    }

    public abstract URI getURI();

    public abstract String getUserReadableString();
}

class TagReference extends UpdateTag {

    private final String tag_name;

    TagReference(final String source_string) {
        super(source_string);
        if (!source_string.startsWith("#")) {
            throw new IllegalArgumentException("Tag reference needs to start with a # sign");
        }
        tag_name = source_string.substring(1);
        if (tag_name.isEmpty()) {
            throw new IllegalArgumentException("Tag reference is empty");
        }
    }

    @Override
    public DataChannel<?> getChannel() {
        return UpdatesDataChannel.getTagChannel(tag_name);
    }

    @Override
    public URI getURI() {
        try {
            return new URI("http", "blip.pl", "/tags/" + tag_name, null);
        } catch (URISyntaxException use) {
            use.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUserReadableString() {
        return "Tag #" + tag_name;
    }
}

class UserReference extends UpdateTag {

    private final String user_name;

    UserReference(final String source_string) {
        super(source_string);
        if (!source_string.startsWith("^")) {
            throw new IllegalArgumentException("User reference needs to start with a ^ sign");
        }
        user_name = source_string.substring(1);
        if (user_name.isEmpty()) {
            throw new IllegalArgumentException("User name reference is empty");
        }
    }

    @Override
    public DataChannel<?> getChannel() {
        return UpdatesDataChannel.getUserChannel(user_name);
    }

    @Override
    public URI getURI() {
        try {
            return new URI("http", "blip.pl", "/users/" + user_name + "/dashboard", null);
        } catch (URISyntaxException use) {
            use.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUserReadableString() {
        return "Kokpit ^" + user_name;
    }
}

class ExternalLink extends UpdateTag {

    private final URI link_uri;

    ExternalLink(String source_string) {
        super(source_string);
        try {
            link_uri = new URI(source_string);
        } catch (URISyntaxException use) {
            throw new IllegalArgumentException(use);
        }
    }

    @Override
    public boolean hasChannel() {
        return false;
    }

    @Override
    public DataChannel<?> getChannel() {
        return null;
    }

    @Override
    public URI getURI() {
        return link_uri;
    }

    @Override
    public String getUserReadableString() {
        return "Link do " + link_uri.toString();
    }

    @Override
    public String toString() {
        return "[link]";
    }
}

class UpdateLink extends UpdateTag {

    private final int update_num;

    private final URI link_uri;

    UpdateLink(String source_string) {
        super(source_string);
        final Matcher update_matcher = Pattern.compile(UPDATE_REGEX).matcher(source_string);
        if (!update_matcher.find()) {
            throw new IllegalArgumentException("Input isn't an update reference.");
        }
        update_num = Integer.parseInt(update_matcher.group(1));
        try {
            link_uri = new URI(source_string);
        } catch (URISyntaxException use) {
            throw new IllegalArgumentException(use);
        }
    }

    @Override
    public boolean hasChannel() {
        return false;
    }

    @Override
    public DataChannel<?> getChannel() {
        return null;
    }

    @Override
    public URI getURI() {
        return link_uri;
    }

    @Override
    public String getUserReadableString() {
        return "Blipnięcie nr " + update_num;
    }

    @Override
    public String toString() {
        return "[blip]";
    }
}
