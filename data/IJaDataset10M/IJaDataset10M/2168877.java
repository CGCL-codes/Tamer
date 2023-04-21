package gnos.info.frame;

import gnos.conf.Configurator;
import gnos.index.Persona;
import gnos.util.Conversion;
import java.io.*;
import java.util.*;

/** Frames are the unit of framexing and search.
 *
 * A Frame is a set of segments.  Each segment is a set of attributes. 
 * Each attribute has a name and a textual value.
 * An attribute may be {@link Attribute#isStored() stored} with the segment, 
 * in which case it is returned with search hits on the document.  
 * Thus each document should typically contain one or more segments and stored 
 * attributes which uniquely identify it.
 *
 * <p>Note that attributes which are <i>not</i> {@link Attribute#isStored() stored} are
 * <i>not</i> available in documents retrieved from the framex, e.g. with {@link
 * Hits#doc(int)}, {@link Searcher#doc(int)} or {@link framexReader#frame(int)}.
 */
public class Frame implements Serializable {

    public Long framex;

    public String title;

    public int lastSegmentx = 0;

    public File dir = null;

    public TreeMap<Integer, Object> segments = new TreeMap<Integer, Object>();

    public String name;

    public long counter = 0;

    public TreeMap personas = new TreeMap();

    public Frame(String title) throws IOException {
        framex = new Long(Configurator.env.frameIndex.lastFramex++);
        this.title = title;
        dir = new File(Configurator.env.frameIndex.dir.getCanonicalPath() + "/$_" + framex + "." + title);
        if (!dir.exists()) {
            dir.mkdir();
        }
        Configurator.env.addFrame(this);
    }

    /**
	* <p>Adds a segment to a document.  Segment name should be unique.
	* the same name.  In this case, if the fields are framexed, their text is
	* treated as though appended for the purposes of search.</p>
	* <p> Note that add like the removeField(s) methods only makes sense 
	* prior to adding a document to an framex. These methods cannot
	* be used to change the content of an existing framex! In order to achieve this,
	* a document has to be deleted from an framex and a new changed version of that
	* document has to be added.</p>
	*/
    public final void addSegment(Segment segment) {
        segment.segmentx = ++lastSegmentx;
        segments.put(new Integer(segment.segmentx), segment);
    }

    public final void keepSegment(Segment segment) throws Exception {
        if (segment == null) return;
        segment.segmentx = ++lastSegmentx;
        writeSegment(segment);
        segments.put(new Integer(segment.segmentx), segment.file);
    }

    /** Returns a segment with the given segment stub if any exist in this frame, or
	* null.  If multiple fields exists with this name, this method returns the
	* first value added.
	*/
    public final Segment getSegment(Integer segmentx) {
        if (!segments.containsKey(segmentx)) {
            return (Segment) segments.get(segmentx);
        } else return null;
    }

    public final void removeSegment(Integer segmentx) {
        segments.remove(segmentx);
    }

    public void writeSegment(Segment segment) throws Exception {
        segment.setFile(this.dir.getCanonicalPath());
        segment.writeAttrib();
        segment.attributes.clear();
        segment.isStored = true;
    }

    public Segment readSegment(File file) throws Exception {
        String tag = Conversion.getSegmentTag(file);
        Segment segment = new Segment(this, tag);
        segment.setFile(this.dir.getCanonicalPath());
        segment.readAttrib();
        segment.isStored = false;
        return segment;
    }

    public File getSegmentFile(int segmentx) {
        Integer key = new Integer(segmentx);
        File file = (File) segments.get(key);
        return file;
    }

    public void listSegments() {
        Set keys = segments.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Integer key = (Integer) iterator.next();
            File file = (File) segments.get(key);
            System.out.println("Segment # " + key + " = " + file);
        }
    }

    public void listPersonas() throws Exception {
        File file = new File("KJV_PERSONAS.txt");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)), true);
        Iterator persona_iterator = personas.keySet().iterator();
        int nn = 0;
        while (persona_iterator.hasNext()) {
            String persona_name = (String) persona_iterator.next();
            Persona persona = (Persona) personas.get(persona_name);
            System.out.println(persona_name + "\t" + persona.linx.size());
            writer.println("<option value='" + persona_name + "'>" + persona_name + "</option>");
            nn++;
        }
        System.out.println(nn + " personas");
        writer.close();
    }

    public void listPersonaLinks() throws Exception {
        File file = new File("KJV_PERSONA_LINKS.txt");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)), true);
        Iterator persona_iterator = personas.keySet().iterator();
        int nn = 0;
        while (persona_iterator.hasNext()) {
            String persona_name = (String) persona_iterator.next();
            Persona persona = (Persona) personas.get(persona_name);
            Iterator linx_iterator = persona.linx.keySet().iterator();
            while (linx_iterator.hasNext()) {
                Integer segx = (Integer) linx_iterator.next();
                Segment segment = (Segment) segments.get(segx);
                ArrayList attribx_list = (ArrayList) persona.linx.get(segx);
                for (int i = 0; i < attribx_list.size(); i++) {
                    Integer attrx = (Integer) attribx_list.get(i);
                    Attrib attrib = (Attrib) segment.attributes.get(attrx);
                    System.out.println(persona_name);
                    writer.println(persona_name);
                    nn++;
                }
            }
        }
        System.out.println(nn + " persona links");
        writer.close();
    }

    public void listTerms() throws Exception {
        File file = new File("KJV_TERMS.txt");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)), true);
        Iterator seg_iterator = segments.keySet().iterator();
        int nn = 0;
        while (seg_iterator.hasNext()) {
            Integer segx = (Integer) seg_iterator.next();
            Segment segment = (Segment) segments.get(segx);
            writer.println("<book codex='" + segment.tag + "'>");
            Iterator term_iterator = segment.terms.keySet().iterator();
            while (term_iterator.hasNext()) {
                String term = (String) term_iterator.next();
                writer.println(term);
                nn++;
            }
            writer.println("</book>");
        }
        System.out.println(nn + " terms");
        writer.close();
    }
}
