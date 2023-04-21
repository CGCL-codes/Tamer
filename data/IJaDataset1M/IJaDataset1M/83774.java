package edu.mta.ok.nworkshop.model;

/**
 * Interface to be implemented by any class that holds a Netflix indexed model data in memory.
 * 
 * The interface assumes the model is indexed by movie IDs and thus define access methods to 
 * the model's data by movie IDs
 * 
 * @see MovieIndexedModelRatings
 * @see MovieIndexedModelResiduals
 */
public interface MovieIndexedModel {

    /**
	 * Sorts the model in ascending order according to the user id
	 */
    public void sortModel();

    /**
	 * Return all the ratings a certain movie got
	 * 
	 * @param movieId The id of the movie we want to get the ratings for
	 * @return An array with all the ratings a certain movie got 
	 * (a byte or double array according to the loaded model) 
	 */
    public Object getMovieRatings(short movieId);

    /**
	 * Return an array of ratings for a given movie
	 * 
	 * @param userInd the index of the movie in the model
	 * @return an array of byte/double ratings (a byte or double array according to the loaded model)
	 */
    public Object getMovieRatingsByIndex(short movieInd);

    /**
	 * Return the IDs of all the users rated a certain movie got
	 * 
	 * @param movieId The id of the movie we want to get the ratings for
	 * @return An array with all the user ids rated a certain movie
	 */
    public int[] getMovieRaters(short movieId);

    /**
	 * Return the IDs of all the users rated a certain movie got
	 * 
	 * @param movieInd an index to the place the IDs are kept in the model
	 * @return An array with all the user IDs rated a certain movie
	 */
    public int[] getMovieRatersByIndex(short movieInd);

    /**
	 * Return an array with the user ids who rated a given movie and their ratings    
	 * 
	 * @param movieId the id of the movie that we want to get data for
	 * @return an array with two elements, the first is an int array with the user IDs, 
	 * and the second is a byte or double array with the users ratings 
	 * (the type is set according to the loaded model)
	 */
    public Object[] getMovieData(short movieId);

    /**
	 * Return an array with the user IDs who rated a given movie and their ratings    
	 * 
	 * @param movieId an index to the place the the data is kept in the model
	 * @return an array with two elements, the first is an int array with the user IDs, 
	 * and the second is a byte or double array with the users ratings 
	 * (the type is set according to the loaded model)
	 */
    public Object[] getMovieDataByIndex(short movieInd);

    /**
	 * Remove the entire data a certain movie has ffrom the model 
	 * 
	 * @param movieInd the index in which the movie data is held in the model
	 */
    public void removeMovieDataByIndex(short movieInd);

    /**
	 * 
	 * @return the entire user ids model
	 */
    public int[][] getUserIds();

    /**
	 * Return a two dimensional array containing the entire ratings data.
	 * 
	 * @return an object array when every element is an array of byte/double ratings that 
	 * the model contain (the array type is set according to the loaded model).
	 */
    public Object[] getRatings();

    /**
	 * Removes the entire ratings model from the memory
	 */
    public void removeRatings();
}
