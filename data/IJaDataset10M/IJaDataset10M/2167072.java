package com.jimsproch.parse;

import java.io.File;
import com.jimsproch.data.InputUtilities;
import com.jimsproch.data.XMLNode;

/**
 * Typically, parsing of unstructured documents is done in three phases:
 * 1.  Extract structure from the unstructured document (XML).  This allows the input data to be manipulated in a reasonable way.
 * 2.  Clean up the unstructured data.  This typically includes trimming fields and making data inferences.
 * 3.  Use the now structured and cleaned up data to produce output data.  This frequently means generating a new XML document in a compliant schema
 * 
 * This parser's job is to handle the first of these three steps.  A parser needs to be configured using an appropriate
 * XML configuration file.  An example file might look something like this:
 * 
 * <matcher class="com.jimsproch.parse.RandomOrderGroupMatcher">
 *	<matcher class="com.jimsproch.parse.RegexMatcher" listener="com.courserank.parsers.kettering.MiscListener" name="termName">Class Schedule for (.*?) [0-9][0-9][0-9][0-9]</matcher>
 *	<matcher class="com.jimsproch.parse.RegexMatcher" name="department">&lt;TD COLSPAN="15" CLASS="ddseparator"&gt;(.*?)&lt;/TD&gt;</matcher>
 *	<matcher class="com.jimsproch.parse.OrderedGroupMatcher" listener="com.courserank.parsers.kettering.CourseListener">
 *		<matcher class="com.jimsproch.parse.RegexMatcher">&lt;TR&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="CCN">&lt;TD CLASS="dddefault"&gt;&lt;SMALL&gt;([0-9]*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="course">&lt;TD.*? CLASS="dddefault".*?&gt;&lt;SMALL&gt;(.*?) (.*?) (.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="credits">&lt;TD CLASS="dddefault"&gt;&lt;SMALL&gt;(.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="title">&lt;TD CLASS="dddefault"&gt;&lt;SMALL&gt;(.*?)&lt;br&gt;&lt;font color=#ff0000&gt;&lt;/font&gt;&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="instructor">&lt;TD CLASS="dddefault"&gt;&lt;SMALL&gt;(.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="monday">&lt;TD CLASS="dddefault"&gt;&lt;SMALL&gt;(.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="tuesday">&lt;TD CLASS="dddefault"&gt;&lt;SMALL&gt;(.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="wednesday">&lt;TD CLASS="dddefault"&gt;&lt;SMALL&gt;(.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="thursday">&lt;TD CLASS="dddefault"&gt;&lt;SMALL&gt;(.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="friday">&lt;TD CLASS="dddefault"&gt;&lt;SMALL&gt;(.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="time">&lt;TD.*? CLASS="dddefault".*?&gt;&lt;SMALL&gt;(.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="room">&lt;TD.*? CLASS="dddefault".*?&gt;&lt;SMALL&gt;(.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher" name="seats">&lt;TD CLASS="dddefault"&gt;&lt;SMALL&gt;(.*?)&lt;/SMALL&gt;&lt;/TD&gt;</matcher>
 *		<matcher class="com.jimsproch.parse.RegexMatcher">&lt;/TR&gt;</matcher>
 *	</matcher>
 * </matcher>
 *
 * Typically, a configuration file will have groups in the outer layers and RegexMatchers to get individual parameters.
 * You can define custom matchers by extending this class (com.jimsproch.parse.ContentMatcher)
 * 
 *  To launch the parser, simply call ContentMatcher.parse(configuration, data);
 * 
 * @author Jim Sproch
 *
 */
public abstract class ContentMatcher {

    private ContentMatcher parent;

    private XMLNode configuration;

    public void setConfiguration(XMLNode configuration) {
        this.configuration = configuration;
    }

    public void setParent(ContentMatcher parent) {
        this.parent = parent;
    }

    protected ContentMatcher getParent() {
        return parent;
    }

    protected XMLNode getConfiguration() {
        return configuration;
    }

    protected abstract MatchResult run(String data);

    public static XMLNode parse(XMLNode configuration, File data) {
        return fromXML(configuration).run(InputUtilities.read(data));
    }

    public static XMLNode parse(XMLNode configuration, String data) {
        return fromXML(configuration).run(data);
    }

    protected static ContentMatcher fromXML(XMLNode root) {
        try {
            ContentMatcher cp = (ContentMatcher) Class.forName(root.getAttribute("class")).newInstance();
            cp.setConfiguration(root);
            return cp;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
