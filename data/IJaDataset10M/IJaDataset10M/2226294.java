package org.s3b.search.resource;

import java.util.ArrayList;
import java.util.List;
import org.foafrealm.manage.Person;
import org.s3b.search.query.QueryParameterEntry;
import org.s3b.search.tools.Visit;

/**
 * @author  Lukasz Porwol
 *
 */
public class MidResource {

    public List<IndexResource> resources;

    public List<QueryParameterEntry> parameters;

    public Person person;

    public boolean used;

    public boolean done;

    public boolean finished;

    public boolean th;

    public double foafBoost;

    public double taxBoost;

    public double wordnetBoost;

    public Thread thr;

    public double recentVisitBoost;

    public double oldVisitBoost;

    public double bookmarksBoost;

    public boolean iffurther;

    public List<String> status = new ArrayList<String>();

    public List<Visit> Visited = new ArrayList<Visit>();

    public boolean ok;

    private MidResource() {
    }

    private static MidResource instance = new MidResource();

    public static MidResource getInstance() {
        return instance;
    }
}
