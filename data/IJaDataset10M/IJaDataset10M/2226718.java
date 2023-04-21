package com.mycila.jms;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Query {

    final StringBuilder sb = new StringBuilder();

    private Query() {
    }

    public Query header(JMSHeader header) {
        sb.append(header);
        return this;
    }

    public Query eq(String val) {
        sb.append("='").append(val).append("'");
        return this;
    }

    public String build() {
        return sb.toString();
    }

    @Override
    public String toString() {
        return build();
    }

    public static Query q() {
        return new Query();
    }
}
