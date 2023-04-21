package net.sf.gap.mc.qagesa.multimedia;

/**
 * This class is responsible to maintain information about a single movie
 * <p>
 * In particular it keeps information about:
 * <ul>
 * <li>its name<li/>
 * <li>its storage format<li/>
 * <li>its total duration in milliseconds<li/>
 * <li>its total size in bytes<li/> <ul/>
 * 
 * @author Giovanni Novelli
 */
public class Movie {

    private String name;

    private String format;

    public Movie(String name, String format) {
        this.setName(name);
        this.setFormat(format);
    }

    public String toString() {
        String msg = "";
        msg += "MOVIE[" + "(NAME, " + this.getName() + "), " + "(FORMAT, " + this.getFormat() + "), " + "]";
        return msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
