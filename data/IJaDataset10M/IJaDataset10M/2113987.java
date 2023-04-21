package org.jbehave.core.story.domain;

public interface Step extends ScenarioComponent {

    void perform(World world) throws Exception;
}
