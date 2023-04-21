package uk.ac.ebi.intact.view.webapp.it;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AdvancedSearchIT extends IntactViewIT {

    @Test
    public void whenIClickOnAddAndSearchIShouldHaveCorrectResults() throws Exception {
        goToTheStartPage();
        showAdvancedFields();
        selectAdvancedFieldByLabel("Pubmed Id");
        typeAdvancedQuery("11554746");
        clickOnAddAndSearch();
        assertThat(numberOfResultsDisplayed(), is(equalTo(4)));
    }

    @Test
    public void whenISelectDetectionMethodUsingTreeIShouldHaveCorrectResults() throws Exception {
        goToTheStartPage();
        showAdvancedFields();
        selectAdvancedFieldByLabel("Interaction detection method");
        clickOnBrowseIcon();
        selectImagingTechniqueInDialog();
        clickOnAddAndSearch();
        assertThat(numberOfResultsDisplayed(), is(equalTo(2)));
    }

    private void typeAdvancedQuery(String search) {
        driver.findElement(By.id("newQuerytxt")).sendKeys(search);
    }

    private void selectAdvancedFieldByLabel(String fieldValue) {
        changeSelectToLabel(By.id("newQueryField"), fieldValue);
        waitUntilLoadingIsComplete();
    }

    private void showAdvancedFields() {
        driver.findElement(By.id("addFieldBtn")).click();
        waitUntilElementIsVisible(By.id("newQuerytxt"));
    }

    private void changeSelectToLabel(By element, String label) {
        Select select = new Select(driver.findElement(element));
        select.selectByVisibleText(label);
    }

    private void clickOnAddAndSearch() {
        driver.findElement(By.id("addAndSearchBtn")).click();
    }

    private void selectImagingTechniqueInDialog() {
        driver.findElement(By.xpath("//li[@id='ontologyTree_node_0']/div/span/span")).click();
        waitUntilElementIsVisible(By.id("ontologyTree:0_1:termTxt"));
        driver.findElement(By.id("ontologyTree:0_1:termTxt")).click();
        waitUntilElementHasValue(By.id("newQuerytxt"), "MI:0428");
    }

    private void clickOnBrowseIcon() {
        waitUntilElementIsVisible(By.id("browseOntologyImg"));
        driver.findElement(By.id("browseOntology")).click();
    }
}
