package org.mitre.scap.xccdf;

import gov.nist.checklists.xccdf.x11.*;

public abstract class XCCDFVisitorHandler {

    public void visitBenchmark(final BenchmarkDocument.Benchmark benchmark) {
    }

    public void visitProfile(final ProfileType profile, final BenchmarkDocument.Benchmark benchmark) {
    }

    public void visitItem(final ItemType item, final BenchmarkDocument.Benchmark benchmark, final GroupType parent) {
    }

    public void visitSelectableItem(final SelectableItemType item, final BenchmarkDocument.Benchmark benchmark, final GroupType parent) {
    }

    public void visitValue(final ValueType value, final BenchmarkDocument.Benchmark benchmark, final GroupType parent) {
    }

    public void visitRule(final RuleType rule, final BenchmarkDocument.Benchmark benchmark, final GroupType parent) {
    }

    public boolean visitGroup(final GroupType group, final BenchmarkDocument.Benchmark benchmark, final GroupType parent) {
        return true;
    }

    public void enterBenchmark(final BenchmarkDocument.Benchmark benchmark) {
    }

    public void enterGroup(final GroupType group, final BenchmarkDocument.Benchmark benchmark, final GroupType parent) {
    }

    public void exitBenchmark(final BenchmarkDocument.Benchmark benchmark) {
    }

    public void exitGroup(final GroupType group, final BenchmarkDocument.Benchmark benchmark, final GroupType parent) {
    }
}
