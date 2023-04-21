package com.googlecode.sarasvati.mem;

import java.util.HashSet;
import java.util.Set;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.env.TokenSetMemberEnv;
import com.googlecode.sarasvati.impl.MapEnv;

public class MemTokenSet implements TokenSet {

    protected final GraphProcess process;

    protected final String name;

    protected final int maxMemberIndex;

    protected boolean complete = false;

    protected Set<ArcToken> activeArcTokens = new HashSet<ArcToken>();

    protected Set<NodeToken> activeNodeTokens = new HashSet<NodeToken>();

    protected Env env = new MapEnv();

    protected TokenSetMemberEnv memberEnv;

    public MemTokenSet(final GraphProcess process, final String name, final int maxMemberIndex) {
        this.process = process;
        this.name = name;
        this.maxMemberIndex = maxMemberIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GraphProcess getProcess() {
        return process;
    }

    @Override
    public Set<ArcToken> getActiveArcTokens(final Engine engine) {
        return activeArcTokens;
    }

    @Override
    public Set<NodeToken> getActiveNodeTokens(final Engine engine) {
        return activeNodeTokens;
    }

    @Override
    public int getMaxMemberIndex() {
        return maxMemberIndex;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public void markComplete(final Engine engine) {
        complete = true;
    }

    @Override
    public void reactivateForBacktrack(final Engine engine) {
        complete = false;
    }

    @Override
    public Env getEnv() {
        return env;
    }

    @Override
    public TokenSetMemberEnv getMemberEnv() {
        if (memberEnv == null) {
            memberEnv = new MemTokenSetMemberEnv(this);
        }
        return memberEnv;
    }
}
