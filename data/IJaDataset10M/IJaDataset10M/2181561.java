package uk.ac.ebi.intact.view.webapp.it;

import org.junit.Test;
import org.openqa.selenium.By;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GoBrowserIT extends IntactViewIT {

    @Test
    public void filterByGoAfterSearchShouldAppendGoQuery() throws Exception {
        goToTheQueryPage("17461779");
        browseByGeneOntology();
        clickOnResponseToStress();
        assertThat(numberOfResultsDisplayed(), is(equalTo(19)));
        assertThat(searchQuery(), is(equalTo("17461779 AND go_expanded_id:\"GO:0006950\"")));
    }

    private void clickOnResponseToStress() {
        expandAndClickOnNode(1, 2);
    }

    private void expandAndClickOnNode(int parent, int node) {
        expandNode(parent);
        clickOnNode(parent + "_" + node);
    }

    private void browseByGeneOntology() {
        driver.findElement(By.id("mainPanels:goBrowseBtn")).click();
    }

    private void expandNode(int node) {
        driver.findElement(By.xpath("//li[@id='mainPanels:goTree_node_" + node + "']/div/span/span")).click();
        waitUntilLoadingIsComplete();
    }

    private void clickOnNode(String nodeId) {
        driver.findElement(By.xpath("//li[@id='mainPanels:goTree_node_" + nodeId + "']/div/span/span[2]")).click();
        waitUntilLoadingIsComplete();
    }
}
