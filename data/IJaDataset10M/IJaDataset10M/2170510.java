package com.bobo._02array;

/**    
 *     
 * @Project_name: dataStructure    
 * @Class_name: _003LowArrayApp    
 * @Description: 简单的做一些封装     
 * @Author: <a href="mailto:bobo2581@gmail.com">bobo</a>    
 * @Create_date：2012-4-6 下午02:10:43    
 * @Modifier: Administrator    
 * @Modification_time：2012-4-6 下午02:10:43    
 * @Modify_note:     
 * @version:      
 *     
 */
public class _003LowArrayApp {

    public static void main(String[] args) {
        _002LowArray arr;
        arr = new _002LowArray(100);
        int nElems = 0;
        int j;
        arr.setElem(0, 77);
        arr.setElem(1, 99);
        arr.setElem(2, 44);
        arr.setElem(3, 55);
        arr.setElem(4, 22);
        arr.setElem(5, 88);
        arr.setElem(6, 11);
        arr.setElem(7, 00);
        arr.setElem(8, 66);
        arr.setElem(9, 33);
        nElems = 10;
        for (j = 0; j < nElems; j++) {
            System.out.print(arr.getElem(j) + " ");
        }
        System.out.println("");
        int searchKey = 26;
        for (j = 0; j < nElems; j++) {
            if (arr.getElem(j) == searchKey) {
                break;
            }
        }
        if (j == nElems) {
            System.out.print("can't find " + searchKey);
        } else {
            System.out.print("find " + searchKey);
        }
        System.out.println("");
        for (j = 0; j < nElems; j++) {
            if (arr.getElem(j) == 55) {
                break;
            }
        }
        for (int k = j; k < nElems; k++) {
            arr.setElem(k, arr.getElem(k + 1));
        }
        nElems--;
        for (j = 0; j < nElems; j++) {
            System.out.print(arr.getElem(j) + " ");
        }
    }
}
