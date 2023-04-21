package org.grailrtls.legacy.gcs.conversion.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.grailrtls.legacy.gcs.conversion.message.GZIPStatisticsMessage.Hub.Landmark;

public class GZIPStatisticsMessage extends GZIPMessage {

    public static final byte MESSAGE_TYPE = 5;

    private long timestamp;

    private float serverBPS;

    private float serverSPS;

    private int numberHubs;

    private List<Hub> hubs;

    public static class Hub {

        private long hubId;

        private int numLandmarks;

        private List<Landmark> landmarks;

        public static class Landmark {

            private int physicalLayer;

            private int antenna;

            private long lastSampleTime;

            private float bytesPerSecond;

            private float samplesPerSecond;

            public int getPhysicalLayer() {
                return physicalLayer;
            }

            public void setPhysicalLayer(int physicalLayer) {
                this.physicalLayer = physicalLayer;
            }

            public int getAntenna() {
                return antenna;
            }

            public void setAntenna(int antenna) {
                this.antenna = antenna;
            }

            public long getLastSampleTime() {
                return lastSampleTime;
            }

            public void setLastSampleTime(long lastSampleTime) {
                this.lastSampleTime = lastSampleTime;
            }

            public float getBytesPerSecond() {
                return bytesPerSecond;
            }

            public void setBytesPerSecond(float bytesPerSecond) {
                this.bytesPerSecond = bytesPerSecond;
            }

            public float getSamplesPerSecond() {
                return samplesPerSecond;
            }

            public void setSamplesPerSecond(float samplesPerSecond) {
                this.samplesPerSecond = samplesPerSecond;
            }
        }

        public long getHubId() {
            return hubId;
        }

        public void setHubId(long hubId) {
            this.hubId = hubId;
        }

        public int getNumLandmarks() {
            return numLandmarks;
        }

        public void setNumLandmarks(int numLandmarks) {
            this.numLandmarks = numLandmarks;
        }

        public List<Landmark> getLandmarks() {
            return landmarks;
        }

        public void setLandmarks(List<Landmark> landmarks) {
            this.landmarks = landmarks;
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getServerBPS() {
        return serverBPS;
    }

    public void setServerBPS(float serverBPS) {
        this.serverBPS = serverBPS;
    }

    public float getServerSPS() {
        return serverSPS;
    }

    public void setServerSPS(float serverSPS) {
        this.serverSPS = serverSPS;
    }

    public int getNumberHubs() {
        return numberHubs;
    }

    public void setNumberHubs(int numberHubs) {
        this.numberHubs = numberHubs;
    }

    public List<Hub> getHubs() {
        return hubs;
    }

    public void setHubs(List<Hub> hubs) {
        this.hubs = hubs;
    }

    @Override
    protected boolean decodeSpecificMessage(byte[] message) throws IOException {
        super.setZippedMessage(message);
        byte[] messageBytes = super.decompressData();
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(messageBytes));
        this.timestamp = in.readLong();
        this.serverBPS = in.readFloat();
        this.serverSPS = in.readFloat();
        this.numberHubs = in.readInt();
        this.hubs = new ArrayList<Hub>();
        for (int hubIndex = 0; hubIndex < this.numberHubs; ++hubIndex) {
            Hub hub = new Hub();
            hub.setHubId(in.readLong());
            hub.setNumLandmarks(in.readInt());
            for (int landmarkIndex = 0; landmarkIndex < hub.getNumLandmarks(); ++landmarkIndex) {
                if (hub.getLandmarks() == null) {
                    hub.setLandmarks(new ArrayList<Landmark>());
                }
                Landmark landmark = new Landmark();
                landmark.setPhysicalLayer(in.readInt());
                landmark.setAntenna(in.readInt());
                landmark.setLastSampleTime(in.readLong());
                landmark.setBytesPerSecond(in.readFloat());
                landmark.setSamplesPerSecond(in.readFloat());
                hub.getLandmarks().add(landmark);
            }
            this.hubs.add(hub);
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        sb.append(" | Server Stats (").append(new Date(this.getTimestamp())).append(") ").append(String.format("%,06.2f", this.getServerBPS())).append("B/s ").append(String.format("%,06.2f", this.getServerSPS())).append("S/s\n");
        if (this.getHubs() != null) {
            for (Hub hub : this.getHubs()) {
                sb.append(" Hub ").append(hub.getHubId()).append("\n");
                if (hub.getLandmarks() != null) {
                    for (Landmark landmark : hub.getLandmarks()) {
                        sb.append("    Landmark (Phy: ").append(landmark.getPhysicalLayer()).append(", Ant: ").append(landmark.getAntenna()).append(") ");
                        sb.append(String.format("%,05.2f", landmark.getBytesPerSecond())).append("B/s ").append(String.format("%,05.2f", landmark.getSamplesPerSecond())).append("S/s\n");
                    }
                }
            }
        }
        return sb.toString();
    }
}
