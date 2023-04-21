package com.googlecode.pinthura.factory.report;

public final class InformationImpl implements Information {

    private final String groupName;

    private final int memberCount;

    public InformationImpl(final String groupName, final int memberCount) {
        this.groupName = groupName;
        this.memberCount = memberCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getMemberCount() {
        return memberCount;
    }
}
