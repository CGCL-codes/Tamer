package com.busdepot.imc;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.busdepot.webapp.domain.ImcPart;
import com.imcparts.schema.CategoryInformationType;
import com.imcparts.schema.LimitedProductInformationType;
import com.imcparts.schema.LineInformationType;
import com.imcparts.schema.ProductInformationType;
import com.imcparts.schema.VehicleInformationType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/spring-service.xml" })
public class ImcServiceTestIntegration {

    private Logger logger = Logger.getLogger(ImcServiceTestIntegration.class);

    @Autowired
    private ImcService imcService;

    @Test
    public void getRandomProduct() {
        List<ProductInformationType> productList = new ArrayList();
        String vid = new String();
        String category = new String();
        String line = new String();
        while (productList.size() == 0) {
            vid = getRandomVID();
            category = getRandomCategory(vid);
            while (category.contentEquals("empty")) {
                vid = getRandomVID();
                category = getRandomCategory(vid);
            }
            line = getRandomLine(vid, category);
            while (line.contains("empty")) {
                vid = getRandomVID();
                category = getRandomCategory(vid);
                line = getRandomLine(vid, category);
            }
            List<String> vidList = new ArrayList();
            vidList.add(vid);
            List<String> lineList = new ArrayList();
            lineList.add(line);
            productList = imcService.getProductInformation(vidList, lineList);
        }
        int productListSize = productList.size();
        Random random = new Random();
        int pickProduct = random.nextInt(productListSize);
        ProductInformationType product = productList.get(pickProduct);
        if (logger.isDebugEnabled()) {
            System.out.println("");
            logger.debug("Random Product name: " + product.getPartName());
            logger.debug("Random Product number: " + product.getPartNumber());
        }
    }

    public String getRandomVID() {
        List<String> makeList = imcService.getMakeInformation();
        int makeListSize = makeList.size();
        Random random = new Random();
        int pickMake = random.nextInt(makeListSize);
        String make = makeList.get(pickMake);
        Random rand = new Random();
        int randomYear = rand.nextInt(60) + 1950;
        String year = Integer.toString(randomYear);
        List<VehicleInformationType> vehicleList = new ArrayList();
        while (vehicleList.size() == 0) {
            vehicleList = imcService.getVehicleInformation(make, year);
            randomYear = rand.nextInt(60) + 1950;
            year = Integer.toString(randomYear);
        }
        int vehicleListSize = vehicleList.size();
        if (vehicleListSize == 0) {
            System.out.println("VehicleSizeList == 0");
        }
        Random randomVid = new Random();
        int pickRandomVid = randomVid.nextInt(vehicleListSize);
        VehicleInformationType vehicle = vehicleList.get(pickRandomVid);
        String vid = vehicle.getVID();
        return vid;
    }

    public String getRandomCategory(String vid) {
        List<String> vidList = new ArrayList();
        vidList.add(vid);
        List<CategoryInformationType> categoryList = imcService.getCategoryInformation(vidList);
        Random random = new Random();
        int categoryListSize = categoryList.size();
        if (categoryListSize == 0) {
            String empty = "empty";
            return empty;
        }
        int pickCategory = random.nextInt(categoryListSize);
        CategoryInformationType category = categoryList.get(pickCategory);
        return category.getCategoryName();
    }

    public String getRandomLine(String vid, String category) {
        List<String> vidList = new ArrayList();
        vidList.add(vid);
        List<String> categoryList = new ArrayList();
        categoryList.add(category);
        List<LineInformationType> lineList = imcService.getLineInformation(vidList, categoryList);
        int lineListSize = lineList.size();
        if (lineListSize == 0) {
            String empty = "empty";
            return empty;
        }
        Random random = new Random();
        int pickLine = random.nextInt(lineListSize);
        LineInformationType randomLine = lineList.get(pickLine);
        String line = randomLine.getLineName();
        return line;
    }

    @Test
    public void getMakeInformation() {
        List<String> makeList = imcService.getMakeInformation();
        assertTrue("makeList.size < 20", makeList.size() >= 20);
        if (logger.isDebugEnabled()) {
            logger.debug("make list:");
            for (String make : makeList) {
                logger.debug(make);
            }
        }
    }

    @Test
    public void getVehicleInformation() {
        String make = "Buick";
        String year = "2001";
        List<VehicleInformationType> vehicleList = imcService.getVehicleInformation(make, year);
        assertTrue("vehicleList.size > 0", vehicleList.size() > 0);
        if (logger.isDebugEnabled()) {
            logger.debug("vehicleList: ");
            for (VehicleInformationType vehicle : vehicleList) {
                logger.debug("VID: " + vehicle.getVID());
            }
        }
    }

    @Test
    public void getCategoryInformation() {
        List<String> vidList = new ArrayList();
        String randomVid = getRandomVID();
        vidList.add(randomVid);
        List<CategoryInformationType> categoryList = imcService.getCategoryInformation(vidList);
        if (logger.isDebugEnabled()) {
            logger.debug("Category Information List: ");
            for (CategoryInformationType category : categoryList) {
                logger.debug("Category: " + category.getCategoryName());
            }
        }
    }

    @Test
    public void getLineInformationTest() {
        List<String> vidList = new ArrayList();
        String vid = getRandomVID();
        vidList.add(vid);
        List<String> categoryList = new ArrayList();
        String category = getRandomCategory(vid);
        categoryList.add(category);
        List<LineInformationType> lineList = imcService.getLineInformation(vidList, categoryList);
        if (logger.isDebugEnabled()) {
            logger.debug("Line information List: ");
            for (LineInformationType line : lineList) {
                logger.debug("Line: " + line.getLineName());
            }
        }
    }

    @Test
    public void getRandomMake() {
        List<String> makeList = imcService.getMakeInformation();
        int listSize = makeList.size();
        Random random = new Random();
        int pickMake = random.nextInt(listSize);
        String make = makeList.get(pickMake);
        if (logger.isDebugEnabled()) {
            logger.debug("Random Make: " + make);
        }
    }

    @Test
    public void getRandomVIDTest() {
        List<String> makeList = imcService.getMakeInformation();
        int makeListSize = makeList.size();
        Random random = new Random();
        int pickMake = random.nextInt(makeListSize);
        String make = makeList.get(pickMake);
        Random rand = new Random();
        int randomYear = rand.nextInt(60) + 1950;
        String year = Integer.toString(randomYear);
        List<VehicleInformationType> vehicleList = new ArrayList();
        while (vehicleList.size() == 0) {
            vehicleList = imcService.getVehicleInformation(make, year);
            randomYear = rand.nextInt(60) + 1950;
            year = Integer.toString(randomYear);
        }
        int vehicleListSize = vehicleList.size();
        Random randomVid = new Random();
        int pickRandomVid = randomVid.nextInt(vehicleListSize);
        VehicleInformationType vehicle = vehicleList.get(pickRandomVid);
        if (logger.isDebugEnabled()) {
            logger.debug("Random Vehicle Vid: " + vehicle.getVID());
        }
    }

    @Test
    public void getRandomCategoryTest() {
        List<String> vidList = new ArrayList();
        String randomVid = getRandomVID();
        vidList.add(randomVid);
        List<CategoryInformationType> categoryList = imcService.getCategoryInformation(vidList);
        Random random = new Random();
        int categoryListSize = categoryList.size();
        int pickCategory = random.nextInt(categoryListSize);
        CategoryInformationType category = categoryList.get(pickCategory);
        if (logger.isDebugEnabled()) {
            logger.debug("Random category: " + category.getCategoryName());
        }
    }
}
