package ca.ucalgary.cpsc.ebe.fitClipse.core.data.impl;

import java.util.ArrayList;
import java.util.List;
import ca.ucalgary.cpsc.ebe.fitClipse.FitClipseProject;
import ca.ucalgary.cpsc.ebe.fitClipse.core.Sha1;
import ca.ucalgary.cpsc.ebe.fitClipse.core.data.IFixtureInfo;
import ca.ucalgary.cpsc.ebe.fitClipse.core.data.ITestCell;
import ca.ucalgary.cpsc.ebe.fitClipse.core.data.ITestTable;
import ca.ucalgary.cpsc.ebe.fitClipse.core.factories.FrameworkFactory;
import ca.ucalgary.cpsc.ebe.fitClipse.core.factories.GreenPepperFactory;
import ca.ucalgary.cpsc.ebe.fitClipse.core.rendering.RenderFit;
import ca.ucalgary.cpsc.ebe.fitClipse.core.rendering.RenderGreenPepper;

public abstract class TestTable implements ITestTable {

    private List<List<ITestCell>> cells;

    private IFixtureInfo fixtureInfo;

    private String id;

    FitClipseProject proj;

    private String tableAttributes;

    public TestTable(FitClipseProject proj) {
        setCells(new ArrayList<List<ITestCell>>());
        fixtureInfo = new FixtureInfo();
        tableAttributes = "";
        this.proj = proj;
    }

    public void addFixture(final String name) {
        fixtureInfo.addFixture(name);
    }

    public void addRow(final int row, final List<ITestCell> value) {
        getCells().add(row, value);
    }

    @Deprecated
    public void clearFixtures() {
        fixtureInfo.clearFixtures();
    }

    public abstract Object clone();

    protected TestTable cloneTo(final TestTable clone) {
        clone.id = id;
        clone.fixtureInfo = (IFixtureInfo) fixtureInfo.clone();
        clone.tableAttributes = tableAttributes;
        clone.cells = new ArrayList<List<ITestCell>>();
        for (final List<ITestCell> cellGroup : cells) {
            final List<ITestCell> cloneGroup = new ArrayList<ITestCell>();
            for (final ITestCell cell : cellGroup) {
                cloneGroup.add((ITestCell) cell.clone());
            }
            clone.cells.add(cloneGroup);
        }
        return clone;
    }

    public int columnCountFor(final int row) {
        if (getCells().get(row) == null) {
            return 0;
        }
        return getCells().get(row).size();
    }

    protected String formatedCell(final String value, boolean first) {
        String result = "";
        final String[] pieces = value.split(" ");
        for (String piece : pieces) {
            piece = piece.trim();
            if (piece.length() >= 1) {
                if (first) {
                    result += Character.toLowerCase(piece.charAt(0));
                    first = false;
                } else {
                    result += Character.toUpperCase(piece.charAt(0));
                }
            }
            if (piece.length() > 1) {
                result += piece.substring(1, piece.length());
            }
        }
        return result;
    }

    public ITestCell getCell(final int row, final int column) {
        return getCells().get(row).get(column);
    }

    public List<List<ITestCell>> getCells() {
        return cells;
    }

    public List<String> getFixtures() {
        final List<String> fixtures = new ArrayList<String>();
        for (final String fixture : fixtureInfo.getFixtures()) {
            fixtures.add(FrameworkFactory.getFactory(proj).normalizeFixture(fixture));
        }
        return fixtures;
    }

    public String getTableAttributes() {
        return tableAttributes;
    }

    public String getUniqueID() {
        return id;
    }

    public StringBuffer render() {
        return FrameworkFactory.getFactory(FitClipseProject.newProject()).render().tables(this);
    }

    public StringBuffer renderMultiModal() {
        return FrameworkFactory.getFactory(FitClipseProject.newProject()).render().multiModalTable(this);
    }

    public int rowCount() {
        return getCells().size();
    }

    public void setCell(final int row, final int column, final String value, final String attributes) {
        if (getCells().get(row) == null) {
            getCells().set(row, new ArrayList<ITestCell>());
        }
        getCells().get(row).set(column, new TestCell(value));
    }

    private void setCells(final List<List<ITestCell>> cells) {
        this.cells = cells;
    }

    public void setTableAttributes(final String attributes) {
        tableAttributes = attributes;
    }

    /**
	 * This method must be called after the fixtures have been added
	 * 
	 * @param testFileName
	 */
    public void setUniqueID(final String testFileName) {
        String idParts = proj.getName();
        for (final String fixInfo : getFixtures()) {
            idParts += fixInfo;
        }
        idParts += testFileName;
        id = Sha1.hash(idParts);
    }

    public String toString() {
        return render().toString();
    }
}
