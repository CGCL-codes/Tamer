package org.mss.quartzjobs.model.jobmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.mss.quartzjobs.CorePlugin;

public class JobReportContentProvider implements IStructuredContentProvider {

    private String filtername;

    /**
	 * MK 24.12.2008
	 * 
	 * JobDetailTableContentProvider gets input from Quartz Download Job
	 * Each Job Run creates one JobReport object. 
	 * 1. User selects Job from JobOverview, clicks on Row.
	 * 2. Selection Listener is JobReportView -> calls update function
	 * 3. Update function calls JobReportManager -> returns report for specified job_id
	 * 4. Update JobReport View
	 * 
	 */
    public void dispose() {
    }

    public void setFilterName(String name) {
        this.filtername = name;
    }

    public Object[] getElements(Object inputElement) {
        List list = new ArrayList<JobReport>();
        Iterator<JobReport> tableiterator = CorePlugin.getDefault().getRepository().allJobReports().iterator();
        while (tableiterator.hasNext()) {
            JobReport report = tableiterator.next();
            if (report.getName().equalsIgnoreCase(filtername)) list.add(report);
        }
        return list.toArray();
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
