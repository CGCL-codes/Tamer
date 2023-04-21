package com.moesol.gwt.maps.shared;

public class Feature {

    public static class Builder {

        private final String m_title;

        private String m_iconUrl;

        private final double m_lat;

        private final double m_lng;

        public Builder(String title, double lat, double lng) {
            m_title = title;
            m_lat = lat;
            m_lng = lng;
        }

        public Builder iconUrl(String iconUrl) {
            m_iconUrl = iconUrl;
            return this;
        }

        public Feature build() {
            return new Feature(m_title, m_lat, m_lng, m_iconUrl);
        }
    }

    private final String m_title;

    private final String m_iconUrl;

    private final double m_lat;

    private final double m_lng;

    public Feature(String title, double lat, double lng, String iconUrl) {
        m_title = title;
        m_lat = lat;
        m_lng = lng;
        m_iconUrl = iconUrl;
    }

    public String getTitle() {
        return m_title;
    }

    public double getLat() {
        return m_lat;
    }

    public double getLng() {
        return m_lng;
    }

    public String getIcon() {
        return m_iconUrl;
    }
}
