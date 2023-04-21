package org.esb.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Job extends MemberMap {

    private MediaFile _infile = null;

    private MediaFile _outfile = null;

    private Date _jobstart = new Date();

    private Date _jobend = new Date(0L);

    private List<JobStream> _streams = new ArrayList<JobStream>();

    public enum STATUS {

        NONE, QUEUED, RUNNING, COMPLETE
    }

    private STATUS _status = STATUS.NONE;

    public Job() {
        super();
    }

    public void setId(int id) {
        setAttribute("id", id);
    }

    public int getId() {
        return getIntegerAttribute("id");
    }

    public void setInputFile(MediaFile infile) {
        _infile = infile;
    }

    public MediaFile getInputFile() {
        return _infile;
    }

    public void setOutputFile(MediaFile file) {
        _outfile = file;
    }

    public MediaFile getOutputFile() {
        return _outfile;
    }

    public void setJobStart(Date date) {
        _jobstart = date;
    }

    public Date getJobStart() {
        return _jobstart;
    }

    public void setJobEnd(Date date) {
        _jobend = date;
    }

    public Date getJobEnd() {
        return _jobend;
    }

    public void setStatus(STATUS s) {
        _status = s;
    }

    public STATUS getStatus() {
        return _status;
    }

    public List<JobStream> getStreams() {
        if (_infile != null && _outfile != null) {
            List<MediaStream> inStreams = _infile.getStreams();
            List<MediaStream> outStreams = _outfile.getStreams();
            int stream_count = Math.min(inStreams.size(), outStreams.size());
            for (int a = 0; a < stream_count; a++) {
                JobStream stream = new JobStream();
                stream.setInputStream(inStreams.get(a));
                stream.setOutputStream(outStreams.get(a));
                _streams.add(stream);
            }
        }
        return _streams;
    }
}
