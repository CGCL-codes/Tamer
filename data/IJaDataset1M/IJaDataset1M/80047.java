package com.leohart.buildwhisperer.adapt;

import com.leohart.buildwhisperer.BuildStatus;
import com.sun.syndication.feed.synd.SyndEntryImpl;

/**
 * @author A122695
 */
public interface SyndEntryImplBuildStatusExtractor {

    BuildStatus getBuildStatus(SyndEntryImpl entry);
}
