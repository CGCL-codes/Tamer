package com.sitescape.team.module.profile.processor;

import java.util.List;
import java.util.Map;
import com.sitescape.team.domain.Binder;
import com.sitescape.team.domain.Definition;
import com.sitescape.team.domain.Principal;
import com.sitescape.team.module.binder.processor.EntryProcessor;
import com.sitescape.team.module.shared.InputDataAccessor;

public interface ProfileCoreProcessor extends EntryProcessor {

    public void syncEntry(Principal entry, InputDataAccessor inputData, Map options);

    public void syncEntries(Map entries, Map options);

    public List syncNewEntries(Binder binder, Definition definition, Class clazz, List inputAccessors, Map options);
}
