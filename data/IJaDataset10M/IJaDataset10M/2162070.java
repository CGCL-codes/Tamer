package com.technosophos.sinciput.commands.course;

import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;
import com.technosophos.rhizome.document.DocumentList;
import com.technosophos.sinciput.commands.ListDocuments;
import com.technosophos.sinciput.types.CourseEnum;
import com.technosophos.rhizome.document.*;

/**
 * Command for getting a list of courses.
 * This class provides a command for retrieving listings of courses in named
 * repositories.
 * @author mbutcher
 *
 */
public class ListCourses extends ListDocuments implements Comparator<RhizomeDocument> {

    String compareKey = null;

    public Map<String, String> narrower() {
        Map<String, String> narrower = new HashMap<String, String>();
        narrower.put(CourseEnum.TYPE.getKey(), CourseEnum.TYPE.getFieldDescription().getDefaultValue());
        return narrower;
    }

    public String[] additionalMetadata() {
        return new String[] { CourseEnum.COURSE_NUMBER.getKey(), CourseEnum.TITLE.getKey(), CourseEnum.COURSE_TIMES.getKey(), CourseEnum.INSTRUCTOR.getKey(), CourseEnum.INSTRUCTOR_EMAIL.getKey() };
    }

    /**
	 * Sort based on document metadata.
	 * @param list The list to sort.
	 * @param key The name of the metadata element to sort on. Assumes natural ordering of strings.
	 */
    protected void sortResults(DocumentList list, String key) {
        this.compareKey = key;
        Collections.sort(list, this);
        return;
    }

    protected void sortResults(DocumentList list) {
        this.compareKey = "title";
        Collections.sort(list, this);
        return;
    }

    public int compare(RhizomeDocument r1, RhizomeDocument r2) {
        String t1 = r1.getMetadatum(this.compareKey).getFirstValue();
        String t2 = r2.getMetadatum(this.compareKey).getFirstValue();
        return t1.compareToIgnoreCase(t2);
    }
}
