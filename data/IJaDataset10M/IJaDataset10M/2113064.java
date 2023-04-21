package pl.edu.agh.ssm.monitor.parsers.sdp;

import pl.edu.agh.ssm.monitor.data.SessionDescription;
import pl.edu.agh.ssm.monitor.data.SessionMedia;
import pl.edu.agh.ssm.monitor.data.SessionMediaType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import pl.edu.agh.ssm.monitor.data.SessionType;
import pl.edu.agh.ssm.monitor.data.SessionDescription.MediaDescription;

/**
 * Simple SDP parser.
 * 
 * @author aneezka
 */
public class SDPParser {

    /**
     * @todo clean this whole mess from the class (printlns, commented lines, etc.)
     * @todo maybe better error handling?
     */
    public SDPParser() {
    }

    public SessionDescription parse(String sdpData) throws InvalidDataException {
        if (sdpData == null || sdpData.trim().equals("")) {
            throw new InvalidDataException("Empty SDP data string");
        }
        String[] params = sdpData.split("\n");
        if (params.length == 0) {
            throw new InvalidDataException("Invalid SDP data (could not recognize any parameters)");
        }
        ParameterList paramList = new ParameterList();
        for (String p : params) {
            Parameter param = parseSingleLine(p);
            paramList.add(param);
        }
        for (String m : mandatory) {
            if (!paramList.contains(m)) {
                throw new InvalidDataException("Invalid SDP data (there is no mandatory parameter " + m + ")");
            }
        }
        boolean b = false;
        for (String t : paramList.getTypes()) {
            b = false;
            for (String a : all) {
                if (a.equals(t)) {
                    b = true;
                    break;
                }
            }
            if (!b) {
                throw new InvalidDataException("Invalid SDP data (unrecognized parameter: " + t + ")");
            }
        }
        SessionDescription sessionDescription = new SessionDescription();
        ParameterList timeDescParams = null;
        List<String> allowedTypes = new LinkedList<String>();
        for (String s : time) {
            allowedTypes.add(s);
        }
        while ((timeDescParams = paramList.retrieveGroup(allowedTypes)) != null) {
            SessionDescription.TimeDescription timeDesc = new SessionDescription.TimeDescription();
            String t = timeDescParams.retrieveSingle("t");
            String[] times = t.split(" ");
            if (times.length != 2) {
                throw new InvalidDataException("Invalid SDP data (\"t\" parameter format: " + t + ")");
            }
            long startTime;
            long endTime;
            try {
                startTime = Long.parseLong(times[0]);
                endTime = Long.parseLong(times[1]);
            } catch (NumberFormatException numberFormatException) {
                throw new InvalidDataException("Invalid SDP data (\"t\" parameter format: " + t + ")");
            }
            if (startTime != 0L) {
                timeDesc.setStartDate(new Date(startTime * 1000L));
            }
            if (endTime != 0L) {
                timeDesc.setEndDate(new Date(endTime * 1000L));
            }
            String r = timeDescParams.retrieveSingle("r");
            if (r != null) {
                String[] rStrings = r.split(" ");
                if (rStrings.length < 3) {
                    throw new InvalidDataException("Invalid SDP data (\"r\" parameter format: " + r + ")");
                }
                timeDesc.setRepeatInterval(rStrings[0]);
                timeDesc.setActiveDuration(rStrings[1]);
                for (int i = 2; i < rStrings.length; i++) {
                    timeDesc.addOffset(rStrings[i]);
                }
            }
            sessionDescription.addTimeDescription(timeDesc);
        }
        ParameterList mediaDescParams = null;
        allowedTypes = new LinkedList<String>();
        for (String s : media) {
            allowedTypes.add(s);
        }
        while ((mediaDescParams = paramList.retrieveGroup(allowedTypes)) != null) {
            SessionDescription.MediaDescription mediaDesc = new SessionDescription.MediaDescription();
            mediaDesc.setMediaName(mediaDescParams.retrieveSingle("m"));
            mediaDesc.setMediaTitle(mediaDescParams.retrieveSingle("i"));
            mediaDesc.setConnectionInfo(mediaDescParams.retrieveSingle("c"));
            mediaDesc.setEncryptionKey(mediaDescParams.retrieveSingle("k"));
            String aParam = null;
            while ((aParam = mediaDescParams.retrieveSingle("b")) != null) {
                mediaDesc.addBandwidth(aParam);
            }
            while ((aParam = mediaDescParams.retrieveSingle("a")) != null) {
                mediaDesc.addMediaAttribute(aParam);
            }
            sessionDescription.addMediaDescription(mediaDesc);
        }
        sessionDescription.setVersion(paramList.retrieveSingle("v"));
        sessionDescription.setName(paramList.retrieveSingle("s"));
        sessionDescription.setOwner(paramList.retrieveSingle("o"));
        sessionDescription.setDescription(paramList.retrieveSingle("i"));
        sessionDescription.setUri(paramList.retrieveSingle("u"));
        sessionDescription.setEmail(paramList.retrieveSingle("e"));
        sessionDescription.setPhone(paramList.retrieveSingle("p"));
        sessionDescription.setConnectionInfo(paramList.retrieveSingle("c"));
        sessionDescription.setTimeZoneAdjustment(paramList.retrieveSingle("z"));
        sessionDescription.setEncryptionKey(paramList.retrieveSingle("e"));
        String attribute = null;
        while ((attribute = paramList.retrieveSingle("b")) != null) {
            sessionDescription.addBandwidth(attribute);
        }
        while ((attribute = paramList.retrieveSingle("a")) != null) {
            sessionDescription.addSessionAttribute(attribute);
        }
        sessionDescription.setType(SessionType.SDP_SESSION);
        parseMediaDetails(sessionDescription);
        return sessionDescription;
    }

    private void parseMediaDetails(SessionDescription sessionDescription) {
        for (MediaDescription media : sessionDescription.getMediaDescriptions()) {
            String[] names = media.getMediaName().split(" ");
            try {
                media.setMediaType(SessionMediaType.valueOf(names[0].toUpperCase()));
            } catch (Exception e) {
                media.setMediaType(SessionMediaType.OTHER);
            }
            try {
                media.setMediaPort(Integer.valueOf(names[1]).intValue());
            } catch (Exception e) {
                media.setMediaPort(0);
            }
            if (names.length > 3) {
                try {
                    media.setPayloadType(Integer.valueOf(names[3]).intValue());
                } catch (Exception e) {
                    media.setPayloadType(0);
                }
            }
            String connectionAddress = media.getConnectionInfo();
            if (connectionAddress == null || connectionAddress == "") {
                connectionAddress = sessionDescription.getConnectionInfo();
            }
            try {
                String address = connectionAddress.split(" ")[2];
                if (address.lastIndexOf('/') > -1) {
                    media.setMediaAddress(address.substring(0, address.lastIndexOf('/')));
                } else {
                    media.setMediaAddress(address);
                }
            } catch (Exception e) {
                media.setMediaAddress("0.0.0.0");
            }
            for (String attribute : media.getMediaAttributes()) {
                if (attribute.startsWith("rtpmap")) {
                    String tmp = attribute.substring(attribute.lastIndexOf(':') + 2, attribute.length()).split(" ")[1];
                    String[] vals = tmp.split("/");
                    SessionMedia sessionMedia = new SessionMedia();
                    sessionMedia.setMediaType(media.getMediaType());
                    sessionMedia.setMediaName(vals[0]);
                    sessionMedia.setMediaFreq(Integer.valueOf(vals[1]));
                    sessionDescription.addMap(media.getPayloadType(), sessionMedia);
                    break;
                }
            }
        }
    }

    private Parameter parseSingleLine(String sdpLine) throws InvalidDataException {
        sdpLine = sdpLine.trim();
        String[] s = sdpLine.split("=", 2);
        if (s.length != 2) {
            throw new InvalidDataException("Invalid SDP data (line: " + sdpLine + ")");
        }
        return new Parameter(s[0].trim(), s[1].trim());
    }

    private class Parameter {

        public Parameter(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        private String type;

        private String value;
    }

    private class ParameterList {

        public void add(Parameter parameter) {
            values.add(parameter.getValue());
            types.add(parameter.getType());
        }

        public void add(String type, String value) {
            types.add(type);
            values.add(value);
        }

        public String retrieveSingle(String type) {
            int index = types.indexOf(type);
            if (index >= 0) {
                String result = values.get(index);
                values.remove(index);
                types.remove(index);
                return result;
            } else {
                return null;
            }
        }

        public ParameterList retrieveGroup(List<String> allowedTypes) {
            if (allowedTypes.size() == 0) {
                return null;
            }
            String type = allowedTypes.get(0);
            int index = types.indexOf(type);
            if (index >= 0) {
                ParameterList result = new ParameterList();
                List<Integer> toRemove = new LinkedList<Integer>();
                result.add(type, values.get(index));
                toRemove.add(index);
                for (int i = index + 1; i < types.size(); i++) {
                    if (!types.get(i).equals(type) && allowedTypes.contains(types.get(i))) {
                        result.add(types.get(i), values.get(i));
                        toRemove.add(index);
                    } else {
                        break;
                    }
                }
                for (Integer r : toRemove) {
                    values.remove(r.intValue());
                    types.remove(r.intValue());
                }
                return result;
            } else {
                return null;
            }
        }

        public boolean contains(String m) {
            return types.contains(m);
        }

        public List<String> getTypes() {
            return types;
        }

        List<String> values = new LinkedList<String>();

        List<String> types = new LinkedList<String>();

        List<String> groupMarks = new LinkedList<String>();
    }

    private String[] mandatory = new String[] { "v", "o", "s", "t", "m" };

    private String[] media = new String[] { "m", "i", "c", "b", "k", "a" };

    private String[] time = new String[] { "t", "r" };

    private String[] all = new String[] { "v", "o", "s", "i", "u", "e", "p", "c", "b", "t", "r", "z", "k", "a", "m" };
}
